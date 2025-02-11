package com.mongs.wear.presentation.pages.main.interaction

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
) {
    Box {
        MainInteractionContent(
            mongVo = mongVo,
            collection = {
                navController.navigate(NavItem.CollectionNested.route)
            },
            walking = {
                navController.navigate(NavItem.Walking.route)
            },
            searchMap = {
                navController.navigate(NavItem.SearchMap.route)
            },
            luckyDraw = {
                navController.navigate(NavItem.LuckyDraw.route)
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
    walking: () -> Unit,
    collection: () -> Unit,
    searchMap: () -> Unit,
    luckyDraw: () -> Unit,
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
                    icon = R.drawable.collection,
                    border = R.drawable.interaction_bnt_orange,
                    onClick = collection,
                )

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.locker,
                    border = R.drawable.interaction_bnt_red,
                    onClick = walking,
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleImageButton(
                    icon = R.drawable.map_search,
                    border = R.drawable.interaction_bnt_blue,
                    onClick = searchMap,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier.size(54.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo_not_open),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                CircleImageButton(
                    icon = R.drawable.locker,
                    border = R.drawable.interaction_bnt_purple,
                    onClick = luckyDraw,
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
