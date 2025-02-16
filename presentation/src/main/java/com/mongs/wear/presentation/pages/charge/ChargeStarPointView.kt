package com.mongs.wear.presentation.pages.charge

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.PageIndicatorState
import androidx.wear.compose.material.Text
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.background.StoreNestedBackground
import com.mongs.wear.presentation.component.common.bar.LoadingBar
import com.mongs.wear.presentation.component.common.button.BlueButton
import com.mongs.wear.presentation.component.common.button.SelectButton
import com.mongs.wear.presentation.component.common.button.YellowButton
import com.mongs.wear.presentation.component.common.pagenation.PageIndicator
import com.mongs.wear.presentation.component.common.textbox.StarPoint
import com.mongs.wear.presentation.global.manager.BillingManager
import kotlin.math.max
import kotlin.math.min

@Composable
fun ChargeStarPointView(
    navController: NavController,
    chargeStarPointViewModel: ChargeStarPointViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val starPoint = chargeStarPointViewModel.starPoint.observeAsState(0)
    val productVoList = chargeStarPointViewModel.productVoList.observeAsState(ArrayList())
    val productVoListIndex = remember { mutableIntStateOf(0) }
    val pageIndicatorState: PageIndicatorState = remember {
        object : PageIndicatorState {
            override val pageOffset: Float
                get() = 0f
            override val selectedPage: Int
                get() = productVoListIndex.intValue
            override val pageCount: Int
                get() = productVoList.value.size
        }
    }

    Box {
        StoreNestedBackground()

        if (chargeStarPointViewModel.uiState.loadingBar) {
            ChargeStarPointLoadingBar()
        } else {
            PageIndicator(
                pageIndicatorState = pageIndicatorState,
                modifier = Modifier.zIndex(1f)
            )
            ChargeStarPointContent(
                productVoListIndex = productVoListIndex.intValue,
                productVoSize = productVoList.value.size,
                preProductVo = { productVoListIndex.intValue = max(0, productVoListIndex.intValue - 1) },
                nextProductVo = { productVoListIndex.intValue = min(productVoListIndex.intValue + 1, productVoList.value.size - 1) },
                productVo = productVoList.value.getOrNull(productVoListIndex.intValue),
                starPoint = starPoint.value,
                consumeOrder = { productId ->
                    chargeStarPointViewModel.consumeOrder(
                        productId = productId
                    )
                },
                productOrder = { productId ->
                    chargeStarPointViewModel.productOrder(
                        activity = context as Activity,
                        productId = productId
                    )
                },
                modifier = Modifier.zIndex(2f),
            )
        }
    }

    LaunchedEffect(chargeStarPointViewModel.uiState.navStoreMenu) {
        if (chargeStarPointViewModel.uiState.navStoreMenu) {
            navController.popBackStack()
        }
    }
}

@Composable
private fun ChargeStarPointContent(
    productVoListIndex: Int,
    productVoSize: Int,
    preProductVo: () -> Unit,
    nextProductVo: () -> Unit,
    productVo: BillingManager.ProductVo?,
    starPoint: Int,
    productOrder: (String) -> Unit,
    consumeOrder: (String) -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        productVo?.let {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f)
                ) {
                    StarPoint(starPoint = starPoint)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f)
                ) {
                    Text(
                        text = productVo.productName,
                        textAlign = TextAlign.Left,
                        fontFamily = DAL_MU_RI,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = MongsWhite,
                        maxLines = 1,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.35f)
                ) {
                    SelectButton(
                        leftBtnDisabled = productVoListIndex == 0,
                        rightBtnDisabled = productVoListIndex == productVoSize - 1,
                        leftBtnClick = preProductVo,
                        rightBtnClick = nextProductVo,
                        modifier = Modifier.zIndex(3f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(R.drawable.point_icon_star),
                                contentDescription = null,
                                modifier = Modifier.size(26.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "+ ${productVo.point}",
                                textAlign = TextAlign.Center,
                                fontFamily = DAL_MU_RI,
                                fontWeight = FontWeight.Light,
                                fontSize = 18.sp,
                                color = MongsWhite,
                                maxLines = 1,
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.25f)
                ) {
                    if (!productVo.hasNotConsumed) {
                        YellowButton(
                            text = productVo.price,
                            height = 33,
                            width = 75,
                            onClick = { productOrder(productVo.productId) },
                        )
                    } else {
                        BlueButton(
                            text = "소비",
                            onClick = { consumeOrder(productVo.productId) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChargeStarPointLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}
