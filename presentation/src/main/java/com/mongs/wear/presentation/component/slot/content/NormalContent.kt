package com.mongs.wear.presentation.component.slot.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.assets.MongResourceCode
import com.mongs.wear.presentation.component.common.charactor.Mong
import com.mongs.wear.presentation.global.viewModel.BaseViewModel

@Composable
fun NormalContent(
    mongVo: MongVo,
    onMongClick: () -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize(),
    ) {
        Mong(
            state = mongVo.statusCode,
            isHappy = BaseViewModel.effectState.isHappy,
            isEating = BaseViewModel.effectState.isEating,
            isSleeping = mongVo.isSleeping,
            mong = MongResourceCode.valueOf(mongVo.mongTypeCode),
            onClick = onMongClick,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .zIndex(1f)
        )
    }
}