package com.paymong.wear.ui.view.main.slot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.paymong.wear.domain.viewModel.code.SlotCode
import com.paymong.wear.domain.viewModel.main.SlotViewModel
import com.paymong.wear.ui.code.MongCode
import com.paymong.wear.ui.code.NavItem
import com.paymong.wear.ui.code.StateCode
import com.paymong.wear.ui.view.common.character.Character

@Composable
fun SlotView(
    navController: NavController,
    showActionContent: () -> Unit,
    mongCode: State<String>,
    stateCode: State<String>,
    poopCount: State<Int>,
    slotViewModel: SlotViewModel = hiltViewModel()
) {
    /** Flag **/
    val processCode = slotViewModel.processCode.observeAsState(SlotCode.STAND_BY)

    /** Logic by ProcessCode **/
    when(processCode.value) {
        SlotCode.GENERATE_MONG -> {
            SlotProcess()
        }
        SlotCode.NAVIGATE -> {
            navController.navigate(NavItem.MainNested.route) {
                popUpTo(
                    navController.graph.id
                )
            }
        }
        else -> {
            SlotContent(
                mongCode = mongCode,
                stateCode = stateCode,
                poopCount = poopCount,
                evolutionStart = slotViewModel::evolutionStart,
                evolutionEnd = slotViewModel::evolutionEnd,
                graduation = slotViewModel::graduation,
                generateMong = slotViewModel::generateMong,
                showSlotActionView = showActionContent
            )
        }
    }
}

@Composable
fun SlotContent(
    mongCode: State<String>,
    stateCode: State<String>,
    poopCount: State<Int>,
    evolutionStart: () -> Unit,
    evolutionEnd: () -> Unit,
    graduation: () -> Unit,
    generateMong: () -> Unit,
    showSlotActionView: () -> Unit
) {
    /** Data **/
    val mong = MongCode.valueOf(mongCode.value)
    val state = StateCode.valueOf(stateCode.value)
    val poop = poopCount.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp)
            .zIndex(0f)
    ) {
        val zIndex = if (state in arrayListOf(
                StateCode.CD006,
                StateCode.CD010
            )
        ) 1f else -1f

        /** Main Layer **/
        Box(
            modifier = Modifier.zIndex(0f)
        ) {
            when (state) {
                StateCode.CD005 -> Dead()
                StateCode.CD007 -> EvolutionReady(evolutionStart = evolutionStart)
                StateCode.CD010 -> {
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Character(mong = mong)
                    }
                }
                StateCode.CD444 -> Empty(onClick = generateMong)
                else -> {
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Character(
                            state = state,
                            mong = mong,
                            showSlotActionView = showSlotActionView
                        )
                    }
                }
            }
        }

        /** Sub Layer **/
        Box(
            modifier = Modifier
                .zIndex(2f)
        ) {
            if (state !in arrayOf(
                    StateCode.CD005,
                    StateCode.CD006,
                    StateCode.CD007,
                    StateCode.CD010,
                    StateCode.CD444
                )
            ) {
                Poop(poop)
            }
        }

        /** Animation Layer **/
        Box(
            modifier = Modifier
                .zIndex(zIndex)
        ) {
            when (state) {
                StateCode.CD009 -> Heart()
                StateCode.CD006 -> GraduationEffect(graduation = graduation)
                StateCode.CD010 -> EvolutionEffect(evolutionEnd = evolutionEnd)
                else -> {}
            }
        }
    }
}