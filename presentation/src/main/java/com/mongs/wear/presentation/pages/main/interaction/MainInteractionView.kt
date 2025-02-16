package com.mongs.wear.presentation.pages.main.interaction

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
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.mongs.wear.core.enums.MongStateCode
import com.mongs.wear.core.errors.PresentationErrorCode
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.MongResourceCode
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.common.button.CircleImageButton

@Composable
fun MainInteractionView(
    navController: NavController,
    mongVo: MongVo?,
    context: Context = LocalContext.current,
) {
    Box {
        MainInteractionContent(
            mongVo = mongVo,
            mapSearch = {
                Toast.makeText(
                    context,
                    "맵 탐색 ${PresentationErrorCode.PRESENTATION_UPDATE_SOON.getMessage()}",
                    Toast.LENGTH_SHORT
                ).show()
                // navController.navigate(NavItem.SearchMap.route)
            },
            luckyDraw = {
                Toast.makeText(
                    context,
                    "뽑기 ${PresentationErrorCode.PRESENTATION_UPDATE_SOON.getMessage()}",
                    Toast.LENGTH_SHORT
                ).show()
                // navController.navigate(NavItem.LuckyDraw.route)
            },
            slotPick = {
                navController.navigate(NavItem.SlotPick.route)
            },
            collection = {
                navController.navigate(NavItem.CollectionNested.route)
            },
            training = {
                navController.navigate(NavItem.TrainingNested.route)
            },
            battle = {
                navController.navigate(NavItem.BattleNested.route)
            },
            exchange = {
                navController.navigate(NavItem.ExchangeNested.route)
            },
            modifier = Modifier.zIndex(1f)
        )
    }
}

@Composable
private fun MainInteractionContent(
    mongVo: MongVo?,
    slotPick: () -> Unit,
    exchange: () -> Unit,
    collection: () -> Unit,
    mapSearch: () -> Unit,
    luckyDraw: () -> Unit,
    training: () -> Unit,
    battle: () -> Unit,
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
                    icon = R.drawable.btn_icon_collection,
                    border = R.drawable.btn_border_orange,
                    iconSize = 34f,
                    onClick = collection,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.point_icon_pay,
                    border = R.drawable.btn_border_purple_dark,
                    disable = mongVo?.let {
                        mongVo.stateCode == MongStateCode.DEAD || mongVo.stateCode == MongStateCode.DELETE
                    } ?: true,
                    onClick = exchange,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleImageButton(
                    icon = R.drawable.btn_icon_map_search,
                    border = R.drawable.btn_border_blue,
                    iconSize = 34f,
                    onClick = mapSearch,
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
                    icon = R.drawable.btn_icon_luck_draw,
                    border = R.drawable.btn_border_purple,
                    iconSize = 34f,
                    onClick = luckyDraw,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleImageButton(
                    icon = R.drawable.btn_icon_activity,
                    border = R.drawable.btn_border_green,
                    iconSize = 34f,
                    disable = mongVo?.let {
                        MongResourceCode.valueOf(mongVo.mongTypeCode).isEgg ||
                        mongVo.stateCode == MongStateCode.DEAD ||
                        mongVo.isSleeping
                    } ?: true,
                    onClick = training,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.btn_icon_battle,
                    border = R.drawable.btn_border_pink,
                    iconSize = 30f,
                    disable = mongVo?.let {
                        MongResourceCode.valueOf(mongVo.mongTypeCode).isEgg ||
                                mongVo.stateCode == MongStateCode.DEAD ||
                                mongVo.isSleeping
                    } ?: true,
                    onClick = battle,
                )
            }
        }
    }
}
