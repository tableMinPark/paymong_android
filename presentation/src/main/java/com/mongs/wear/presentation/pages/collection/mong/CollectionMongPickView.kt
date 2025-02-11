package com.mongs.wear.presentation.pages.collection.mong

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.PositionIndicator
import com.mongs.wear.domain.collection.vo.MongCollectionVo
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.MongResourceCode
import com.mongs.wear.presentation.component.background.CollectionNestedBackground
import com.mongs.wear.presentation.component.common.bar.LoadingBar
import com.mongs.wear.presentation.component.common.button.CircleImageButton
import com.mongs.wear.presentation.component.common.button.CircleTextButton
import com.mongs.wear.presentation.dialog.collection.MongCollectionDetailDialog
import kotlin.math.min

@Composable
fun CollectionMongPickView(
    navController: NavController,
    collectionMongPickViewModel: CollectionMongPickViewModel = hiltViewModel(),
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 0)

    val mongCollectionIndex = remember { mutableIntStateOf(-1) }
    val mongCollectionVoList = collectionMongPickViewModel.mongCollectionVoList.observeAsState(ArrayList())

    Box {
        if (collectionMongPickViewModel.uiState.loadingBar) {
            CollectionNestedBackground()
            CollectionMongPickLoadingBar(modifier = Modifier.zIndex(1f))
        } else {
            CollectionNestedBackground()
            CollectionMongPickContent(
                listState = listState,
                mongCollectionPick = {
                    mongCollectionIndex.intValue = it
                    collectionMongPickViewModel.uiState.detailDialog = true
                },
                mongCollectionVoList = mongCollectionVoList.value,
                modifier = Modifier.zIndex(1f)
            )

            if (collectionMongPickViewModel.uiState.detailDialog && mongCollectionIndex.intValue >= 0) {
                MongCollectionDetailDialog(
                    name = mongCollectionVoList.value[mongCollectionIndex.intValue].name,
                    mongCode = mongCollectionVoList.value[mongCollectionIndex.intValue].code,
                    onClick = { collectionMongPickViewModel.uiState.detailDialog = false },
                    modifier = Modifier.zIndex(2f),
                )
            }
        }
    }

    LaunchedEffect(collectionMongPickViewModel.uiState.navCollectionMenu) {
        if (collectionMongPickViewModel.uiState.navCollectionMenu) {
            navController.popBackStack()
        }
    }
}

@Composable
private fun CollectionMongPickContent(
    listState: LazyListState,
    mongCollectionPick: (Int) -> Unit,
    mongCollectionVoList: List<MongCollectionVo>,
    modifier: Modifier = Modifier.zIndex(0f),
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        PositionIndicator(lazyListState = listState)
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(vertical = 40.dp),
            state = listState,
        ) {
            for (startIndex: Int in 1..mongCollectionVoList.size step (3)) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .height(59.dp)
                            .padding(bottom = 5.dp)
                    ) {
                        for (index: Int in startIndex..min(mongCollectionVoList.size, startIndex + 2)) {
                            val mongCollectionVo = mongCollectionVoList[index - 1]

                            if (!mongCollectionVo.isIncluded) {
                                CircleTextButton(
                                    text = "?",
                                    border = R.drawable.interaction_bnt_darkpurple,
                                    onClick = {},
                                    modifier = Modifier
                                        .offset(
                                            y = if (index % 3 == 2) (-27).dp else 0.dp,
                                            x = 0.dp
                                        )
                                )
                            } else {
                                CircleImageButton(
                                    icon = MongResourceCode.valueOf(mongCollectionVo.code).pngCode,
                                    border = R.drawable.interaction_bnt_darkpurple,
                                    onClick = { mongCollectionPick(index - 1) },
                                    modifier = Modifier
                                        .offset(
                                            y = if (index % 3 == 2) (-27).dp else 0.dp,
                                            x = 0.dp
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CollectionMongPickLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}