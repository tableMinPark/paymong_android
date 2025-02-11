package com.mongs.wear.presentation.dialog.slot

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
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mongs.wear.core.enums.MongStateCode
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.MongResourceCode
import com.mongs.wear.presentation.component.common.button.CircleImageButton

@Composable
fun SlotInteractionDialog(
    mongVo: MongVo,
    feed: () -> Unit,
    slotPick: () -> Unit,
    sleeping: () -> Unit,
    exchangeWalking: () -> Unit,
    poopClean: () -> Unit,
    stroke: () -> Unit,
    inventory: () -> Unit,
    close: () -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    val isEgg = MongResourceCode.valueOf(mongVo.mongTypeCode).isEgg

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Black.copy(alpha = 0.85f))
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = close,
            )
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
                    icon = R.drawable.feed,
                    border = R.drawable.interaction_bnt_yellow,
                    disable = isEgg || mongVo.isSleeping || mongVo.stateCode == MongStateCode.DEAD,
                    onClick = feed,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.pointlogo,
                    iconSize = 22,
                    border = R.drawable.interaction_bnt_darkpurple,
                    disable = mongVo.stateCode == MongStateCode.DEAD || mongVo.stateCode == MongStateCode.DELETE,
                    onClick = exchangeWalking,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleImageButton(
                    icon = R.drawable.sleep,
                    border = R.drawable.interaction_bnt_blue,
                    disable = isEgg || mongVo.stateCode == MongStateCode.DEAD,
                    onClick = sleeping,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.slot,
                    border = R.drawable.interaction_bnt_red,
                    onClick = slotPick,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.poop,
                    border = R.drawable.interaction_bnt_purple,
                    disable = isEgg || mongVo.stateCode == MongStateCode.DEAD || mongVo.isSleeping,
                    onClick = poopClean,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleImageButton(
                    icon = R.drawable.locker,
                    border = R.drawable.interaction_bnt_green,
                    onClick = inventory,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.heart,
                    iconSize = 22,
                    border = R.drawable.interaction_bnt_pink,
                    disable = isEgg || mongVo.stateCode == MongStateCode.DEAD || mongVo.isSleeping,
                    onClick = stroke,
                )
            }
        }
    }
}