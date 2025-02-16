package com.mongs.wear.presentation.pages.training.menu

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import com.mongs.wear.core.errors.PresentationErrorCode
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.background.TrainingNestedBackground
import com.mongs.wear.presentation.component.common.chip.IconChip

@Composable
fun TrainingMenuView(
    navController: NavController,
    context: Context = LocalContext.current,
) {
    Box {
        TrainingNestedBackground()
        TrainingMenuContent(
            jumping = {
                navController.navigate(route = NavItem.TrainingJumping.route)
            },
            basketball = {
                PresentationErrorCode.PRESENTATION_UPDATE_SOON.let { errorCode ->
                    if (errorCode.isMessageShow()) {
                        Toast.makeText(
                            context,
                            errorCode.getMessage(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier.zIndex(1f),
        )
    }
}

@Composable
private fun TrainingMenuContent(
    jumping: () -> Unit,
    basketball: () -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    val listState = rememberScalingLazyListState(initialCenterItemIndex = 0)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        PositionIndicator(scalingLazyListState = listState)
        ScalingLazyColumn(
            contentPadding = PaddingValues(vertical = 60.dp, horizontal = 6.dp),
            modifier = Modifier.fillMaxSize(),
            state = listState,
            autoCentering = null,
        ) {
            item {
                IconChip(
                    icon = R.drawable.btn_icon_runner,
                    border = R.drawable.btn_border_green,
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "달리기",
                    onClick = jumping,
                )
            }

            item {
                IconChip(
                    icon = R.drawable.btn_icon_basketball,
                    border = R.drawable.btn_border_orange,
                    fontColor = Color.White,
                    backgroundColor = Color.Black,
                    label = "농구",
                    onClick = basketball,
                )
            }
        }
    }
}
