package com.mongs.wear.presentation.pages.main.configure

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.mongs.wear.core.errors.PresentationErrorCode
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.common.button.CircleImageButton
import com.mongs.wear.presentation.component.common.button.CircleTextButton

@Composable
fun MainConfigureView(
    navController: NavController,
    context: Context = LocalContext.current
) {
    Box {
        MainConfigureContent(
            notice = {
                Toast.makeText(
                    context,
                    "공지 ${PresentationErrorCode.PRESENTATION_UPDATE_SOON.getMessage()}",
                    Toast.LENGTH_SHORT
                ).show()
                //navController.navigate(NavItem.Notice.route)
            },
            chargeStarPoint = {
                navController.navigate(NavItem.ChargeStarPoint.route)
            },
            feedback = {
                navController.navigate(NavItem.Feedback.route)
            },
            setting = {
                navController.navigate(NavItem.Setting.route)
            },
            help = {
                navController.navigate(NavItem.HelpNested.route)
            },
            modifier = Modifier.zIndex(1f)
        )
    }

}

@Composable
private fun MainConfigureContent(
    notice: () -> Unit,
    chargeStarPoint: () -> Unit,
    feedback: () -> Unit,
    setting: () -> Unit,
    help: () -> Unit,
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
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleTextButton(
                    text = "i",
                    border = R.drawable.btn_border_purple_dark,
                    onClick = help,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
                    .offset(y = (-14).dp)
            ) {
                CircleImageButton(
                    icon = R.drawable.btn_icon_charge,
                    border = R.drawable.btn_border_purple_dark,
                    onClick = chargeStarPoint,
                )

                Spacer(modifier = Modifier.width(48.dp))

                CircleImageButton(
                    icon = R.drawable.btn_icon_notice,
                    border = R.drawable.btn_border_purple_dark,
                    onClick = notice,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
                    .offset(y = (-8).dp)
            ) {
                CircleImageButton(
                    icon = R.drawable.btn_icon_feedback,
                    border = R.drawable.btn_border_purple_dark,
                    onClick = feedback,
                )

                Spacer(modifier = Modifier.width(10.dp))

                CircleImageButton(
                    icon = R.drawable.btn_icon_setting,
                    border = R.drawable.btn_border_purple_dark,
                    onClick = setting,
                )
            }
        }
    }
}