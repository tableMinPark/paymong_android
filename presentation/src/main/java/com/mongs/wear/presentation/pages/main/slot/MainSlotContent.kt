package com.mongs.wear.presentation.pages.main.slot

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.mongs.wear.core.enums.MongStateCode
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.assets.MongResourceCode
import com.mongs.wear.presentation.component.main.slot.content.DeadContent
import com.mongs.wear.presentation.component.main.slot.content.DeleteContent
import com.mongs.wear.presentation.component.main.slot.content.EmptyContent
import com.mongs.wear.presentation.component.main.slot.content.GraduatedContent
import com.mongs.wear.presentation.component.main.slot.content.NormalContent
import com.mongs.wear.presentation.pages.main.slot.MainSlotViewModel.UiState

@Composable
fun MainSlotContent(
    mongVo: MongVo?,
    isPageChanging: Boolean,
    stroke: (Long) -> Unit,
    navSlotPick: () -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
    uiState: UiState,
) {
    mongVo?.let {
        val isEgg = MongResourceCode.valueOf(mongVo.mongTypeCode).isEgg

        when (mongVo.stateCode) {
            MongStateCode.NORMAL -> {
                NormalContent(
                    mongVo = mongVo,
                    stroke = {
                        if (!isEgg && !mongVo.isSleeping) {
                            stroke(mongVo.mongId)
                        }
                    },
                    modifier = modifier,
                )
            }

            MongStateCode.DEAD -> {
                DeadContent(
                    modifier = modifier,
                )
            }

            MongStateCode.GRADUATE_READY -> {
                NormalContent(
                    mongVo = mongVo,
                    stroke = {},
                    modifier = modifier,
                )
            }

            MongStateCode.EVOLUTION_READY -> {
                NormalContent(
                    mongVo = mongVo,
                    stroke = {},
                    modifier = modifier,
                )
            }

            MongStateCode.GRADUATE -> {
                GraduatedContent(
                    mongVo = mongVo,
                    isPageChanging = isPageChanging,
                    modifier = modifier,
                )
            }

            MongStateCode.DELETE -> {
                DeleteContent(
                    isPageChanging = isPageChanging,
                    onClick = navSlotPick,
                    modifier = modifier,
                )
            }

            else -> {}
        }
    } ?: run {
        EmptyContent(
            onClick = navSlotPick,
            modifier = modifier,
        )
    }
}