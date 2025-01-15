package com.mongs.wear.presentation.pages.main.slot

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.assets.NavItem

@Composable
fun MainSlotView(
    navController: NavController,
    mongVo: MongVo?,
    isPageChanging: State<Boolean>,
    mainSlotViewModel: MainSlotViewModel = hiltViewModel(),
) {
    Box {
        MainSlotContent(
            mongVo = mongVo,
            isPageChanging = isPageChanging.value,
            stroke = { mongId -> mainSlotViewModel.stroke(mongId = mongId) },
            navSlotPick = { navController.navigate(NavItem.SlotPick.route) },
            modifier = Modifier.zIndex(1f),
            uiState = mainSlotViewModel.uiState,
        )

        mongVo?.let {
            MainSlotEffect(
                mongVo = mongVo,
                isPageChanging = isPageChanging.value,
                evolution = { mongId ->
                    mainSlotViewModel.evolution(mongId)
                },
                graduationReady = {
                    mainSlotViewModel.graduationReady(mongId = mongVo.mongId)
                },
                modifier = Modifier.zIndex(2f),
                uiState = mainSlotViewModel.uiState,
            )
        }
    }
}