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
    exchange: () -> Unit,
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
                    icon = R.drawable.btn_icon_feed,
                    border = R.drawable.btn_border_yellow,
                    iconSize = 34f,
                    disable = isEgg || mongVo.isSleeping || mongVo.stateCode == MongStateCode.DEAD,
                    onClick = feed,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.btn_icon_stroke,
                    border = R.drawable.btn_border_pink,
                    iconSize = 34f,
                    disable = isEgg || mongVo.stateCode == MongStateCode.DEAD || mongVo.isSleeping,
                    onClick = stroke,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleImageButton(
                    icon = R.drawable.btn_icon_sleep,
                    border = R.drawable.btn_border_blue,
                    iconSize = 34f,
                    disable = isEgg || mongVo.stateCode == MongStateCode.DEAD,
                    onClick = sleeping,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.btn_icon_slot_pick,
                    border = R.drawable.btn_border_red,
                    iconSize = 34f,
                    onClick = slotPick,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.btn_icon_poop_clean,
                    border = R.drawable.btn_border_purple,
                    iconSize = 34f,
                    disable = isEgg || mongVo.stateCode == MongStateCode.DEAD || mongVo.isSleeping,
                    onClick = poopClean,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleImageButton(
                    icon = R.drawable.btn_icon_inventory,
                    border = R.drawable.btn_border_green,
                    iconSize = 34f,
                    onClick = inventory,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.point_icon_pay,
                    border = R.drawable.btn_border_purple_dark,
                    disable = mongVo.stateCode == MongStateCode.DEAD || mongVo.stateCode == MongStateCode.DELETE,
                    onClick = exchange,
                )
            }
        }
    }
}