package com.mongs.wear.presentation.pages.main.configure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.common.button.CircleImageButton
import com.mongs.wear.presentation.component.common.button.CircleTextButton

@Composable
fun MainConfigureView(
    navController: NavController,
) {
    Box {
        MainConfigureContent(
            payment = {
                navController.navigate(NavItem.StoreNested.route)
            },
            help = {
                navController.navigate(NavItem.HelpNested.route)
            },
            feedback = {
                navController.navigate(NavItem.Feedback.route)
            },
            logout = {
                navController.navigate(NavItem.Setting.route)
            },
            modifier = Modifier.zIndex(1f)
        )
    }

}

@Composable
private fun MainConfigureContent(
    payment: () -> Unit,
    help: () -> Unit,
    feedback: () -> Unit,
    logout: () -> Unit,
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
                CircleImageButton(
                    icon = R.drawable.point_store,
                    border = R.drawable.interaction_bnt_darkpurple,
                    onClick = payment,
                )

                Spacer(modifier = Modifier.width(12.dp))

                CircleTextButton(
                    text = "i",
                    border = R.drawable.interaction_bnt_darkpurple,
                    onClick = help,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleImageButton(
                    icon = R.drawable.feedback,
                    border = R.drawable.interaction_bnt_darkpurple,
                    onClick = feedback,
                )

                Spacer(modifier = Modifier.width(12.dp))

                CircleImageButton(
                    icon = R.drawable.setting,
                    border = R.drawable.interaction_bnt_darkpurple,
                    onClick = logout,
                )
            }
        }
    }
}