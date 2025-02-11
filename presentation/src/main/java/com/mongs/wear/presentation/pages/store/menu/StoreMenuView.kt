package com.mongs.wear.presentation.pages.store.menu

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.mongs.wear.core.errors.PresentationErrorCode
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.background.StoreNestedBackground
import com.mongs.wear.presentation.component.common.bar.LoadingBar

@Composable
fun StoreMenuView(
    navController: NavController,
    storeMenuViewModel: StoreMenuViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
) {
    Box {
        StoreNestedBackground()

        if (storeMenuViewModel.uiState.loadingBar) {
            StoreMenuLoadingBar()
        } else {
            val mongVo = storeMenuViewModel.mongVo.observeAsState()
            val isBillingDevice = storeMenuViewModel.isBillingDevice.observeAsState(false)

            StoreMenuContent(
                navExchangePayPoint = {
                    mongVo.value?.let {
                        navController.navigate(NavItem.StoreExchangePayPoint.route)
                    } ?: run {
                        PresentationErrorCode.PRESENTATION_MANAGEMENT_NOT_PICK_SLOT.let { errorCode ->
                            if (errorCode.isMessageShow()) {
                                Toast.makeText(
                                    context,
                                    errorCode.getMessage(),
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                    }
                },
                navChargeStarPoint = {
                    if (isBillingDevice.value) {
                        navController.navigate(NavItem.StoreChargeStarPoint.route)
                    } else {
                        PresentationErrorCode.PRESENTATION_USER_BILLING_NOT_SUPPORT.let { errorCode ->
                            if (errorCode.isMessageShow()) {
                                Toast.makeText(
                                    context,
                                    errorCode.getMessage(),
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                    }
                },
                modifier = Modifier.zIndex(1f)
            )
        }
    }
}

@Composable
private fun StoreMenuContent(
    navExchangePayPoint: () -> Unit,
    navChargeStarPoint: () -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.49f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = navExchangePayPoint
                    ),
            ) {
                Image(
                    painter = painterResource(R.drawable.pointlogo),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.FillBounds,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "환전",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 18.sp,
                    color = MongsWhite,
                    maxLines = 1,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(0.02f)
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(2.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.49f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = navChargeStarPoint,
                    ),
            ) {
                Image(
                    painter = painterResource(R.drawable.starpoint_logo),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.FillBounds,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "충전",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    color = MongsWhite,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun StoreMenuLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}