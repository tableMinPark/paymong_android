package com.mongs.wear.presentation.pages.store.chargeStartPoint

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.player.usecase.GetStarPointUseCase
import com.mongs.wear.domain.store.exception.ConsumeProductOrderException
import com.mongs.wear.domain.store.exception.GetConsumedOrderIdsException
import com.mongs.wear.domain.store.exception.GetProductIdsException
import com.mongs.wear.domain.store.usecase.ConsumeProductOrderUseCase
import com.mongs.wear.domain.store.usecase.GetConsumedOrderIdsUseCase
import com.mongs.wear.domain.store.usecase.GetProductIdsUseCase
import com.mongs.wear.presentation.global.exception.BillingConnectException
import com.mongs.wear.presentation.global.exception.BillingNotSupportException
import com.mongs.wear.presentation.global.exception.GetProductException
import com.mongs.wear.presentation.global.manager.BillingManager
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StoreChargeStarPointViewModel @Inject constructor(
    private val billingManager: BillingManager,
    private val getStarPointUseCase: GetStarPointUseCase,
    private val getProductIdsUseCase: GetProductIdsUseCase,
    private val getConsumedOrderIdsUseCase: GetConsumedOrderIdsUseCase,
    private val consumeProductOrderUseCase: ConsumeProductOrderUseCase,
): BaseViewModel() {

    val starPoint: LiveData<Int> get() = _starPoint
    private val _starPoint = MediatorLiveData<Int>()

    private val _productVoList = MediatorLiveData<List<BillingManager.ProductVo>>(ArrayList())
    val productVoList: LiveData<List<BillingManager.ProductVo>> get() = _productVoList

    // 결제 실패 오류 메시지
    private val billingErrorEvent: SharedFlow<ErrorException> = billingManager.errorEvent

    // 결제 중단 플래그
    private val billingAbortEvent: SharedFlow<Long> = billingManager.abortEvent

    // 결제 성공 플래그
    private val billingSuccessEvent: SharedFlow<Long> = billingManager.successEvent

    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

            _starPoint.addSource(withContext(Dispatchers.IO) { getStarPointUseCase() }) { starPoint ->
                _starPoint.value = starPoint
            }

            getProducts()
        }
    }

    private suspend fun getProducts() {

        uiState.loadingBar = true

        withContext(Dispatchers.IO) {
            val productIds = getProductIdsUseCase()
            val productVoList = billingManager.getProductList(productIds)
            val orderVoList = billingManager.getOrder()

            val consumedOrderIds = getConsumedOrderIdsUseCase(
                GetConsumedOrderIdsUseCase.Param(
                    orderIds = orderVoList.map { it.orderId }
                )
            )
            val orderedProductIdList = orderVoList
                .filter { it.orderId !in consumedOrderIds }
                .map { it.productId }

            _productVoList.postValue(productVoList.map { productVo ->
                if (orderedProductIdList.contains(productVo.productId)) {
                    BillingManager.ProductVo(
                        productId = productVo.productId,
                        point = productVo.point,
                        productName = productVo.productName,
                        price = productVo.price,
                        hasNotConsumed = true
                    )
                } else productVo
            })
        }

        uiState.loadingBar = false
    }

    fun productOrder(activity: Activity, productId: String) {

        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            uiState.loadingBar = true

            billingManager.payProduct(activity = activity, productId = productId)
        }

        // 소비 실패 이벤트
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            billingErrorEvent.collect { errorException ->
                throw errorException
            }
        }

        // 소비 중단 이벤트
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            billingAbortEvent.collect {
                getProducts()
                uiState.loadingBar = false
            }
        }

        // 소비 성공 이벤트
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            billingSuccessEvent.collect {

                getProducts()

                uiState.loadingBar = false

                toastEvent("충전 성공")
            }
        }
    }

    fun consumeOrder(productId: String) {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            uiState.loadingBar = true

            val orderVoList = billingManager.getOrder()

            for (orderVo in orderVoList) {
                if (orderVo.productId == productId) {

                    consumeProductOrderUseCase(
                        ConsumeProductOrderUseCase.Param(
                            productId = orderVo.productId,
                            orderId = orderVo.orderId,
                            purchaseToken = orderVo.purchaseToken
                        )
                    )
                    // 소비 성공
                    getProducts()
                    break
                }
            }

            uiState.loadingBar = false
        }
    }

    val uiState: UiState = UiState()

    class UiState : BaseUiState() {
        var navStoreMenu by mutableStateOf(false)
    }

    override fun exceptionHandler(exception: Throwable) {

        when(exception) {
            is BillingConnectException -> {
                uiState.loadingBar = false
                uiState.navStoreMenu = true
            }

            is BillingNotSupportException -> {
                uiState.loadingBar = false
                uiState.navStoreMenu = true
            }

            is GetProductException -> {
                uiState.loadingBar = false
                uiState.navStoreMenu = true
            }

            is GetProductIdsException -> {
                uiState.loadingBar = false
                uiState.navStoreMenu = true
            }

            is GetConsumedOrderIdsException -> {
                uiState.loadingBar = false
                uiState.navStoreMenu = true
            }

            is ConsumeProductOrderException -> {
                uiState.loadingBar = false

                CoroutineScope(Dispatchers.IO).launch {
                    getProducts()
                }
            }

            else -> {
                uiState.loadingBar = false
            }
        }
    }
}