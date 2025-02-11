package com.mongs.wear.presentation.pages.store.exchangePayPoint

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.background.StoreNestedBackground
import com.mongs.wear.presentation.component.common.bar.LoadingBar
import com.mongs.wear.presentation.component.common.button.SelectButton
import com.mongs.wear.presentation.component.common.button.YellowButton
import com.mongs.wear.presentation.component.common.textbox.PayPoint
import com.mongs.wear.presentation.dialog.common.ConfirmAndCancelDialog
import com.mongs.wear.presentation.global.constValue.StoreConst
import com.mongs.wear.presentation.pages.store.exchangePayPoint.StoreExchangePayPointViewModel.UiState
import kotlin.math.max
import kotlin.math.min

@Composable
fun StoreExchangePayPointView(
    navController: NavController,
    storeExchangePayPointViewModel: StoreExchangePayPointViewModel = hiltViewModel(),
) {

    val mongVo = storeExchangePayPointViewModel.mongVo.observeAsState()
    val ratio = remember { mutableIntStateOf(0) }
    val totalStarPoint = storeExchangePayPointViewModel.starPoint.observeAsState(0)
    val starPoint = remember { derivedStateOf { totalStarPoint.value - ratio.intValue } }
    val chargePayPoint = remember { derivedStateOf { StoreConst.STAR_POINT_PER * ratio.intValue } }

    Box {
        StoreNestedBackground()

        if (storeExchangePayPointViewModel.uiState.loadingBar) {
            StoreExchangePayPointLoadingBar()
        } else if(storeExchangePayPointViewModel.uiState.exchangeStarPointDialog) {
            ConfirmAndCancelDialog(
                text = "$${chargePayPoint.value}\n환전하시겠습니까?",
                confirm = {
                    mongVo.value?.let {
                        storeExchangePayPointViewModel.exchangeStarPoint(
                            mongId = it.mongId,
                            starPoint = ratio.intValue,
                        )
                    }
                    ratio.intValue = 0
                },
                cancel = { storeExchangePayPointViewModel.uiState.exchangeStarPointDialog = false }
            )
        } else {
            StoreExchangePayPointContent(
                ratio = ratio.intValue,
                decreaseRatio = { ratio.intValue = max(ratio.intValue - 1, 0) },
                increaseRatio = { ratio.intValue = min(ratio.intValue + 1, totalStarPoint.value) },
                mongVo = mongVo.value,
                chargePayPoint = chargePayPoint.value,
                starPoint = starPoint.value,
                totalStarPoint = totalStarPoint.value,
                uiState = storeExchangePayPointViewModel.uiState,
                modifier = Modifier.zIndex(1f)
            )
        }
    }
}

@Composable
private fun StoreExchangePayPointContent(
    ratio: Int,
    decreaseRatio: () -> Unit,
    increaseRatio: () -> Unit,
    mongVo: MongVo?,
    chargePayPoint: Int,
    starPoint: Int,
    totalStarPoint: Int,
    uiState: UiState,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
            ) {
                PayPoint(payPoint = mongVo?.payPoint ?: 0)
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f)
            ) {
                Image(
                    painter = painterResource(R.drawable.starpoint_logo),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "X $starPoint",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    color = MongsWhite,
                    maxLines = 1,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                SelectButton(
                    leftBtnDisabled = ratio == 0,
                    rightBtnDisabled = ratio == totalStarPoint,
                    leftBtnClick = decreaseRatio,
                    rightBtnClick = increaseRatio,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.pointlogo),
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            contentScale = ContentScale.FillBounds,
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "+ $chargePayPoint",
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
                YellowButton(
                    text = "환전",
                    width = 70,
                    disable = chargePayPoint == 0 || mongVo == null,
                    onClick = { uiState.exchangeStarPointDialog = true },
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
private fun StoreExchangePayPointLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}