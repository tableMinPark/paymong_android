package com.mongs.wear.presentation.view.collectionMapPick

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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mongs.wear.domain.vo.MapCollectionVo
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.global.component.background.CollectionNestedBackground
import com.mongs.wear.presentation.global.component.button.CircleImageButton
import com.mongs.wear.presentation.global.component.button.CircleTextButton
import com.mongs.wear.presentation.global.component.common.LoadingBar
import com.mongs.wear.presentation.global.dialog.collection.MapCollectionDetailDialog
import com.mongs.wear.presentation.global.resource.MapResourceCode
import com.mongs.wear.presentation.viewModel.collectionMapPick.CollectionMapPickViewModel
import kotlin.math.min

@Composable
fun CollectionMapPickView(
    navController: NavController,
    collectionMapPickViewModel: CollectionMapPickViewModel = hiltViewModel(),
) {
    val mapCollectionIndex = remember { mutableIntStateOf(-1) }
    val mapCollectionVoList = collectionMapPickViewModel.mapCollectionVoList.observeAsState(ArrayList())
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 0)

    Box {
        if (collectionMapPickViewModel.uiState.loadingBar) {
            CollectionNestedBackground()
            CollectionMapPickLoadingBar(modifier = Modifier.zIndex(1f))
        } else if (collectionMapPickViewModel.uiState.detailDialog && mapCollectionIndex.intValue >= 0) {
            MapCollectionDetailDialog(
                mapCode = mapCollectionVoList.value[mapCollectionIndex.intValue].code,
                setBackground = { mapCode ->
                    collectionMapPickViewModel.setBackground(mapCode)
                },
                onClick = { collectionMapPickViewModel.uiState.detailDialog = false },
                modifier = Modifier.zIndex(1f)
            )
        } else {
            CollectionNestedBackground()
            CollectionMapPickContent(
                listState = listState,
                mapCollectionPick = {
                    mapCollectionIndex.intValue = it
                    collectionMapPickViewModel.uiState.detailDialog = true
                },
                mapCollectionVoList = mapCollectionVoList.value,
                modifier = Modifier.zIndex(1f)
            )
        }
    }

    LaunchedEffect(collectionMapPickViewModel.uiState.navCollectionMenu) {
        if (collectionMapPickViewModel.uiState.navCollectionMenu) {
            navController.popBackStack()
            collectionMapPickViewModel.uiState.navCollectionMenu = false
        }
    }
}

@Composable
private fun CollectionMapPickLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        LoadingBar()
    }
}


@Composable
private fun CollectionMapPickContent(
    listState: LazyListState,
    mapCollectionPick: (Int) -> Unit,
    mapCollectionVoList: List<MapCollectionVo>,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(vertical = 40.dp),
            state = listState,
        ) {
            for (startIndex: Int in 1..mapCollectionVoList.size step (3)) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .height(59.dp)
                            .padding(bottom = 5.dp)
                    ) {
                        for (index: Int in startIndex..min(mapCollectionVoList.size, startIndex + 2)) {
                            val mapCollectionVo = mapCollectionVoList[index - 1]

                            if (mapCollectionVo.disable) {
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
                                    icon = MapResourceCode.valueOf(mapCollectionVo.code).code,
                                    border = R.drawable.interaction_bnt_darkpurple,
                                    onClick = { mapCollectionPick(index - 1) },
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

@Preview(showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun CollectionMapPickViewPreview() {
    Box {
        CollectionNestedBackground()
        CollectionMapPickContent(
            listState = rememberLazyListState(0),
            mapCollectionPick = {},
            mapCollectionVoList = arrayListOf(
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
            ),
            modifier = Modifier.zIndex(1f)
        )
    }
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_LARGE_ROUND)
@Composable
private fun LargeCollectionMapPickViewPreview() {
    Box {
        CollectionNestedBackground()
        CollectionMapPickContent(
            listState = rememberLazyListState(0),
            mapCollectionPick = {},
            mapCollectionVoList = arrayListOf(
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
                MapCollectionVo(),
            ),
            modifier = Modifier.zIndex(1f)
        )
    }
}