package com.mongs.wear.presentation.pages.main.interaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mongs.wear.core.enums.MongStateCode
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.MongResourceCode
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.common.button.CircleImageButton

@Composable
fun MainInteractionView(
    navController: NavController,
    mongVo: MongVo?,
    mainInteractionViewModel: MainInteractionViewModel = hiltViewModel(),
) {
    Box {
        MainInteractionContent(
            mongVo = mongVo,
            feed = {
                navController.navigate(NavItem.FeedNested.route)
            },
            collection = {
                navController.navigate(NavItem.CollectionNested.route)
            },
            sleeping = {
                mongVo?.let {
                    mainInteractionViewModel.sleeping(mongId = mongVo.mongId)
                }
            },
            slotPick = {
                navController.navigate(NavItem.SlotPick.route)
            },
            poopClean = {
                mongVo?.let {
                    mainInteractionViewModel.poopClean(mongId = mongVo.mongId)
                }
            },
            training = {
                navController.navigate(NavItem.TrainingNested.route)
            },
            battle = {
                navController.navigate(NavItem.BattleNested.route)
            },
            modifier = Modifier.zIndex(1f)
        )
    }
}

@Composable
private fun MainInteractionContent(
    mongVo: MongVo?,
    feed: () -> Unit,
    collection: () -> Unit,
    sleeping: () -> Unit,
    slotPick: () -> Unit,
    poopClean: () -> Unit,
    training: () -> Unit,
    battle: () -> Unit,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
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
                    disable = mongVo?.let {
                        MongResourceCode.valueOf(mongVo.mongTypeCode).isEgg ||
                        mongVo.isSleeping ||
                        mongVo.stateCode == MongStateCode.DEAD
                    } ?: true,
                    onClick = feed,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.collection,
                    border = R.drawable.interaction_bnt_orange,
                    onClick = collection,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleImageButton(
                    icon = R.drawable.sleep,
                    border = R.drawable.interaction_bnt_blue,
                    disable = mongVo?.let {
                        MongResourceCode.valueOf(mongVo.mongTypeCode).isEgg ||
                        mongVo.stateCode == MongStateCode.DEAD
                    } ?: true,
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
                    disable = mongVo?.let {
                        MongResourceCode.valueOf(mongVo.mongTypeCode).isEgg ||
                        mongVo.stateCode == MongStateCode.DEAD ||
                        mongVo.isSleeping
                    } ?: true,
                    onClick = poopClean,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleImageButton(
                    icon = R.drawable.activity,
                    border = R.drawable.interaction_bnt_green,
                    disable = mongVo?.let {
                        MongResourceCode.valueOf(mongVo.mongTypeCode).isEgg ||
                        mongVo.stateCode == MongStateCode.DEAD ||
                        mongVo.isSleeping
                    } ?: true,
                    onClick = training,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.battle,
                    border = R.drawable.interaction_bnt_pink,
//                    disable = mongVo?.let {
//                        MongResourceCode.valueOf(mongVo.mongTypeCode).isEgg ||
//                                mongVo.stateCode == MongStateCode.DEAD ||
//                                mongVo.isSleeping
//                    } ?: true,
                    onClick = battle,
                )
            }
        }
    }
}
