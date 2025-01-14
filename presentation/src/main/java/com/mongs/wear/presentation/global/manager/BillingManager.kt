package com.mongs.wear.presentation.global.manager

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ConnectionState
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.store.usecase.ConsumeProductOrderUseCase
import com.mongs.wear.presentation.global.exception.BillingConnectException
import com.mongs.wear.presentation.global.exception.BillingNotSupportException
import com.mongs.wear.presentation.global.exception.GetProductException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class BillingManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val consumeProductOrderUseCase: ConsumeProductOrderUseCase,
) : PurchasesUpdatedListener {

    private val _errorEvent = MutableSharedFlow<ErrorException>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _abortEvent = MutableSharedFlow<Long>()
    val abortEvent = _abortEvent.asSharedFlow()

    private val _successEvent = MutableSharedFlow<Long>()
    val successEvent = _successEvent.asSharedFlow()

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder()
            .enableOneTimeProducts()
            .build())
        .build()

    /**
     * 구매 성공 콜백
     */
    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {

        if (billingResult.responseCode == BillingResponseCode.OK && purchases != null) {
            CoroutineScope(Dispatchers.IO).launch {
                for (purchase in purchases) {
                    val productId = purchase.products.get(0)
                    val orderId = purchase.orderId ?: "-"
                    val purchaseToken = purchase.purchaseToken

                    try {
                        consumeProductOrderUseCase(
                            ConsumeProductOrderUseCase.Param(
                                productId = productId,
                                orderId = orderId,
                                purchaseToken = purchaseToken
                            )
                        )

                        _successEvent.emit(System.currentTimeMillis())

                    } catch (exception: ErrorException) {

                        _errorEvent.emit(exception)
                    }
//                    val consumeResult = this@BillingManager.consumeProduct(purchase)
                }
            }
        } else if (billingResult.responseCode in listOf(
                BillingResponseCode.USER_CANCELED,
                BillingResponseCode.ERROR,
                BillingResponseCode.ITEM_ALREADY_OWNED)
            ) {

            CoroutineScope(Dispatchers.IO).launch {
                _abortEvent.emit(System.currentTimeMillis())
            }
        }
    }

    /**
     * BillingClient 연결
     */
    private suspend fun getConnectedBillingClient(): BillingClient = suspendCancellableCoroutine { cont ->

        if (billingClient.connectionState == ConnectionState.DISCONNECTED) {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingResponseCode.OK) {
                        cont.resume(billingClient)
                    } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        cont.resumeWithException(BillingNotSupportException())
                    } else {
                        cont.resumeWithException(BillingConnectException())
                    }
                }

                override fun onBillingServiceDisconnected() {}
            })
        } else {
            cont.resume(billingClient)
        }
    }

    /**
     * 상품 목록 조회 (Google)
     */
    suspend fun getProductList(productIds: List<String>) : List<ProductVo> {

        val productList = productIds.map { productId ->
            Product.newBuilder()
                .setProductId(productId)
                .setProductType(ProductType.INAPP)
                .build()
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        return withContext(Dispatchers.IO) {

            val productDetailsResult = getConnectedBillingClient().queryProductDetails(params)

            if (productDetailsResult.billingResult.responseCode == BillingResponseCode.OK) {

                productDetailsResult.productDetailsList?.let { productDetailsList ->
                    productDetailsList.map { productDetail ->
                        ProductVo(
                            productId = productDetail.productId,
                            productName = productDetail.name,
                            point = productDetail.description.toInt() ?: 0,
                            price = productDetail.oneTimePurchaseOfferDetails?.formattedPrice ?: "",
                            hasNotConsumed = false
                        )
                    }
                } ?: run {
                    throw GetProductException()
                }
            } else {
                throw GetProductException()
            }
        }
    }

    /**
     * 상품 구매 처리
     */
    suspend fun payProduct(activity: Activity, productId: String) {

        val productList = listOf(
            Product.newBuilder()
                .setProductId(productId)
                .setProductType(ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        val productDetailsResult = getConnectedBillingClient().queryProductDetails(params)

        if (productDetailsResult.billingResult.responseCode == BillingResponseCode.OK) {
            productDetailsResult.productDetailsList?.let { productDetailsList ->

                val productDetailsParamsList = listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetailsList.get(0))
                        .build()
                )

                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()

                getConnectedBillingClient().launchBillingFlow(activity, billingFlowParams)
            }
        }
    }

    suspend fun getOrder() = suspendCancellableCoroutine { cont ->

        val purchasesParams = QueryPurchasesParams.newBuilder()
            .setProductType(ProductType.INAPP)
            .build()

        val billingClient = runBlocking { getConnectedBillingClient() }

        billingClient.queryPurchasesAsync(purchasesParams, { billingResult, purchases ->
            if (billingResult.responseCode == BillingResponseCode.OK) {
                cont.resume(purchases.map { purchase ->
                    OrderVo(
                        productId = purchase.products.get(0),
                        orderId = purchase.orderId ?: "-",
                        purchaseToken = purchase.purchaseToken,
                    )
                })
            } else {
                cont.resumeWithException(BillingConnectException())
            }
        })
    }

//    /**
//     * 상품 소비 처리
//     * Backend 에서 처리하도록 변경
//     */
//    private suspend fun consumeProduct(purchase : Purchase): ConsumeResult {
//
//        val consumeParams =
//            ConsumeParams.newBuilder()
//                .setPurchaseToken(purchase.purchaseToken)
//                .build()
//
//        return withContext(Dispatchers.IO) {
//            billingClient.consumePurchase(consumeParams)
//        }
//    }

    data class ProductVo(

        val productId: String,

        val productName: String,

        val point: Int,

        val price: String,

        val hasNotConsumed: Boolean
    )

    data class OrderVo (

        val productId: String,

        val orderId: String,

        val purchaseToken: String,
    )
}