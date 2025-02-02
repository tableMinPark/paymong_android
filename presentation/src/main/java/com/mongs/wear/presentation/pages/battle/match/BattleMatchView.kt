package com.mongs.wear.presentation.pages.battle.match

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.wear.compose.material.Text
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import com.mongs.wear.core.enums.MatchRoundCode
import com.mongs.wear.core.enums.MatchStateCode
import com.mongs.wear.domain.battle.vo.MatchPlayerVo
import com.mongs.wear.domain.battle.vo.MatchVo
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongResourceCode
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.assets.NavItem
import com.mongs.wear.presentation.component.common.background.BattleMatchBackground
import com.mongs.wear.presentation.component.common.bar.HpBar
import com.mongs.wear.presentation.component.common.bar.LoadingBar
import com.mongs.wear.presentation.component.common.charactor.Mong
import com.mongs.wear.presentation.dialog.battle.MatchOverDialog
import com.mongs.wear.presentation.dialog.battle.MatchPickDialog
import com.mongs.wear.presentation.pages.battle.enums.BattleConst
import kotlin.math.max


@Composable
fun BattleMatchView(
    navController: NavController,
    battleMatchViewModel: BattleMatchViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val battlePayPoint = battleMatchViewModel.battlePayPoint.observeAsState(0)
    val matchVo = battleMatchViewModel.matchVo.observeAsState()
    val myMatchPlayerVo = battleMatchViewModel.myMatchPlayerVo.observeAsState()
    val otherMatchPlayerVo = battleMatchViewModel.otherMatchPlayerVo.observeAsState()

    Box {
        BattleMatchBackground()

        if (battleMatchViewModel.uiState.loadingBar) {
            BattleMatchEnterLoadingBar()
        } else {
            matchVo.value?.let { matchVo ->
                if (battleMatchViewModel.uiState.matchOverLoadingBar) {
                    BattleMatchLoadingBar()
                } else if (battleMatchViewModel.uiState.matchPickDialog) {
                    MatchPickDialog(
                        maxSeconds = BattleConst.MAX_SECONDS,
                        attack = {
                            battleMatchViewModel.matchPick(
                                roomId = matchVo.roomId,
                                playerId = myMatchPlayerVo.value?.playerId,
                                targetPlayerId = otherMatchPlayerVo.value?.playerId,
                                pickCode = MatchRoundCode.MATCH_PICK_ATTACK,
                            )
                        },
                        defence = {
                            battleMatchViewModel.matchPick(
                                roomId = matchVo.roomId,
                                playerId = myMatchPlayerVo.value?.playerId,
                                targetPlayerId = myMatchPlayerVo.value?.playerId,
                                pickCode = MatchRoundCode.MATCH_PICK_DEFENCE,
                            )
                        },
                        heal = {
                            battleMatchViewModel.matchPick(
                                roomId = matchVo.roomId,
                                playerId = myMatchPlayerVo.value?.playerId,
                                targetPlayerId = myMatchPlayerVo.value?.playerId,
                                pickCode = MatchRoundCode.MATCH_PICK_HEAL,
                            )
                        },
                        modifier = Modifier.zIndex(1f),
                    )
                } else if (battleMatchViewModel.uiState.matchOverDialog) {
                    MatchOverDialog(
                        battlePayPoint = battlePayPoint.value,
                        matchEnd = battleMatchViewModel::matchEnd,
                        myMatchPlayerVo = myMatchPlayerVo.value,
                        modifier = Modifier.zIndex(1f),
                    )
                } else if (matchVo.stateCode == MatchStateCode.MATCH_ENTER) {
                    BattleMatchEnterContent(
                        myMatchPlayerVo = myMatchPlayerVo.value,
                        otherMatchPlayerVo = otherMatchPlayerVo.value,
                        modifier = Modifier.zIndex(1f),
                    )
                } else {
                    BattleMatchContent(
                        matchVo = matchVo,
                        myMatchPlayerVo = myMatchPlayerVo.value,
                        otherMatchPlayerVo = otherMatchPlayerVo.value,
                        modifier = Modifier.zIndex(1f),
                    )
                }
            }
        }
    }

    matchVo.value?.let {
        LaunchedEffect(it.stateCode) {
            when(it.stateCode) {
                MatchStateCode.MATCH_ENTER -> battleMatchViewModel.matchStart(roomId = it.roomId)
                MatchStateCode.MATCH_WAIT -> battleMatchViewModel.nextRound()
                MatchStateCode.MATCH_FIGHT -> battleMatchViewModel.nextRound()
                MatchStateCode.MATCH_OVER -> battleMatchViewModel.matchOver(roomId = it.roomId)
                else -> {}
            }
        }
    }

    LaunchedEffect(battleMatchViewModel.uiState.navMainEvent) {
        battleMatchViewModel.uiState.navMainEvent.collect {
            navController.popBackStack(route = NavItem.BattleNested.route, inclusive = true)
        }
    }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    DisposableEffect(currentBackStackEntry) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                matchVo.value?.let {
                    if (it.stateCode == MatchStateCode.MATCH_OVER) {
                        battleMatchViewModel.matchEnd()
                    } else {
                        battleMatchViewModel.matchExit(roomId = it.roomId)
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val network = battleMatchViewModel.network.observeAsState(true)

    LaunchedEffect(network.value) {
        if (!network.value) {
            matchVo.value?.let {
                if (it.stateCode != MatchStateCode.MATCH_OVER) {
                    battleMatchViewModel.matchEnd()
                } else {
                    battleMatchViewModel.matchExit(roomId = it.roomId)
                }
            } ?: run {
                battleMatchViewModel.matchEnd()
            }
        }
    }
}

@Composable
private fun BattleMatchEnterContent(
    myMatchPlayerVo: MatchPlayerVo?,
    otherMatchPlayerVo: MatchPlayerVo?,
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
            ) {
                Spacer(modifier = Modifier.width(15.dp))

                otherMatchPlayerVo?.let {
                    Mong(
                        mong = MongResourceCode.valueOf(otherMatchPlayerVo.mongTypeCode),
                        ratio = 0.6f
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
            ) {
                Image(
                    painter = painterResource(R.drawable.battle),
                    contentDescription = null,
                    modifier = Modifier
                        .height(35.dp)
                        .width(45.dp),
                    contentScale = ContentScale.FillBounds
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
            ) {
                Spacer(modifier = Modifier.width(15.dp))

                myMatchPlayerVo?.let {
                    Mong(
                        mong = MongResourceCode.valueOf(myMatchPlayerVo.mongTypeCode),
                        ratio = 0.6f
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

                Image(
                    painter = painterResource(R.drawable.battle_me),
                    contentDescription = null,
                    modifier = Modifier
                        .height(20.dp)
                        .width(40.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Composable
private fun BattleMatchContent(
    matchVo: MatchVo,
    myMatchPlayerVo: MatchPlayerVo?,
    otherMatchPlayerVo: MatchPlayerVo?,
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
            ) {

                if (matchVo.stateCode == MatchStateCode.MATCH_PICK_WAIT) {
                    Box(modifier = Modifier) {
                        LoadingBar()
                    }
                }

                Spacer(modifier = Modifier.width(15.dp))

                otherMatchPlayerVo?.let {
                    MatchPlayer(
                        matchEffect = matchVo.stateCode in listOf(MatchStateCode.MATCH_FIGHT, MatchStateCode.MATCH_OVER) ,
                        matchPlayerVo = otherMatchPlayerVo,
                        effectAlignment = Alignment.BottomStart,
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
            ) {
                HpBar(hp = myMatchPlayerVo?.hp?.toFloat() ?: 0F)

                Text(
                    text = "${max(matchVo.round, 1)}/${BattleConst.MAX_ROUND}",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    color = MongsWhite,
                    maxLines = 1,
                )

                HpBar(hp = otherMatchPlayerVo?.hp?.toFloat() ?: 0F)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
            ) {
                Spacer(modifier = Modifier.width(15.dp))

                myMatchPlayerVo?.let {
                    MatchPlayer(
                        matchEffect = matchVo.stateCode in listOf(MatchStateCode.MATCH_FIGHT, MatchStateCode.MATCH_OVER) ,
                        matchPlayerVo = myMatchPlayerVo,
                        effectAlignment = Alignment.TopEnd,
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

                Image(
                    painter = painterResource(R.drawable.battle_me),
                    contentDescription = null,
                    modifier = Modifier
                        .height(20.dp)
                        .width(40.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Composable
private fun MatchPlayer(
    matchPlayerVo: MatchPlayerVo,
    matchEffect: Boolean = true,
    effectAlignment: Alignment,
) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components { add(ImageDecoderDecoder.Factory()) }
        .build()

    Box {
        Mong(
            mong = MongResourceCode.valueOf(matchPlayerVo.mongTypeCode),
            ratio = 0.6f,
            modifier = Modifier.zIndex(1f)
        )

        if (matchEffect) {
            Box(
                modifier = Modifier
                    .align(effectAlignment)
                    .zIndex(2f)
            ) {
                when (matchPlayerVo.roundCode) {
                    MatchRoundCode.NONE -> {}
                    MatchRoundCode.MATCH_HEAL -> {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.health,
                                imageLoader = imageLoader
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .zIndex(2f)
                                .height(40.dp)
                                .width(40.dp),
                            contentScale = ContentScale.FillBounds
                        )
                    }

                    MatchRoundCode.MATCH_ATTACKED_HEAL -> {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.health,
                                imageLoader = imageLoader
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .zIndex(2f)
                                .height(40.dp)
                                .width(40.dp),
                            contentScale = ContentScale.FillBounds
                        )
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.attack_motion,
                                imageLoader = imageLoader
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .zIndex(3f)
                                .height(40.dp)
                                .width(40.dp),
                            contentScale = ContentScale.FillBounds
                        )
                    }

                    MatchRoundCode.MATCH_ATTACKED -> {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.attack_motion,
                                imageLoader = imageLoader
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .zIndex(2f)
                                .height(40.dp)
                                .width(40.dp),
                            contentScale = ContentScale.FillBounds
                        )
                    }

                    MatchRoundCode.MATCH_DEFENCE -> {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.defence_motion,
                                imageLoader = imageLoader
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .zIndex(2f)
                                .height(40.dp)
                                .width(40.dp),
                            contentScale = ContentScale.FillBounds
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun BattleMatchLoadingBar(
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
private fun BattleMatchEnterLoadingBar(
    modifier: Modifier = Modifier.zIndex(0f),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            ) {
                LoadingBar()
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                Text(
                    text = "배틀 입장중",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 18.sp,
                    color = MongsWhite,
                    maxLines = 1,
                )
            }
        }
    }
}