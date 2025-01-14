package com.mongs.wear.presentation.pages.battle.match

import android.content.Context
import android.view.WindowManager
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import com.mongs.wear.presentation.component.common.button.BlueButton
import com.mongs.wear.presentation.component.common.charactor.Mong
import com.mongs.wear.presentation.dialog.battle.MatchPickDialog
import com.mongs.wear.presentation.pages.battle.enums.BattleConst
import kotlinx.coroutines.delay
import kotlin.math.max


@Composable
fun BattleMatchView(
    navController: NavController,
    battleMatchViewModel: BattleMatchViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    context: Context = LocalContext.current,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    DisposableEffect(currentBackStackEntry) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                battleMatchViewModel.matchExit()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    DisposableEffect(Unit) {
        val window = (context as ComponentActivity).window
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        onDispose {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    val matchVo = battleMatchViewModel.matchVo.observeAsState(MatchVo())
    val myMatchPlayerVo = battleMatchViewModel.myMatchPlayerVo.observeAsState(MatchPlayerVo())
    val otherMatchPlayerVo = battleMatchViewModel.otherMatchPlayerVo.observeAsState(MatchPlayerVo())

    Box {
        if (battleMatchViewModel.uiState.isLoading) {
            BattleMatchBackground()
            BattleMatchLoadingBar()
        } else {
            BattleMatchBackground()
            if (battleMatchViewModel.uiState.matchPickDialog) {
                MatchPickDialog(
                    maxSeconds = BattleConst.MAX_SECONDS,
                    attack = { battleMatchViewModel.matchPick(MatchRoundCode.MATCH_PICK_ATTACK) },
                    defence = { battleMatchViewModel.matchPick(MatchRoundCode.MATCH_PICK_DEFENCE) },
                    heal = { battleMatchViewModel.matchPick(MatchRoundCode.MATCH_PICK_HEAL) },
                    modifier = Modifier.zIndex(1f),
                )
            } else if (matchVo.value.stateCode == MatchStateCode.MATCH_ENTER) {
                BattleMatchEnterContent(
                    matchStart = { battleMatchViewModel.matchStart() },
                    myMatchPlayerVo = myMatchPlayerVo.value,
                    otherMatchPlayerVo = otherMatchPlayerVo.value,
                    modifier = Modifier.zIndex(1f),
                )
            } else if (matchVo.value.stateCode == MatchStateCode.MATCH_OVER) {
                BattleMatchOverContent(
                    navBattleMenu = { navController.popBackStack(route = NavItem.BattleNested.route, inclusive = true) },
                    myMatchPlayerVo = myMatchPlayerVo.value,
                    modifier = Modifier.zIndex(1f),
                )
            } else {
                BattleMatchContent(
                    nextRound = { battleMatchViewModel.uiState.matchPickDialog = true },
                    matchOver = { battleMatchViewModel.matchOver() },
                    matchVo = matchVo.value,
                    myMatchPlayerVo = myMatchPlayerVo.value,
                    otherMatchPlayerVo = otherMatchPlayerVo.value,
                    modifier = Modifier.zIndex(1f),
                )
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

@Composable
private fun BattleMatchEnterContent(
    matchStart: () -> Unit,
    myMatchPlayerVo: MatchPlayerVo,
    otherMatchPlayerVo: MatchPlayerVo,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    LaunchedEffect(Unit) {
        delay(3000)
        matchStart()
    }

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
                Mong(
                    mong = MongResourceCode.valueOf(otherMatchPlayerVo.mongTypeCode),
                    ratio = 0.6f
                )
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
                Mong(
                    mong = MongResourceCode.valueOf(myMatchPlayerVo.mongTypeCode),
                    ratio = 0.6f
                )
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
    nextRound: () -> Unit,
    matchOver: () -> Unit,
    matchVo: MatchVo,
    myMatchPlayerVo: MatchPlayerVo,
    otherMatchPlayerVo: MatchPlayerVo,
    modifier: Modifier = Modifier.zIndex(0f),
) {
    if (matchVo.stateCode == MatchStateCode.MATCH_MATCHING) {
        LaunchedEffect(matchVo.stateCode) {
            delay(3000)
            if (matchVo.isLastRound) {
                matchOver()
            } else {
                nextRound()
            }
        }
    }

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
                MatchPlayer(
                    matchPlayerVo = otherMatchPlayerVo,
                    effectAlignment = Alignment.BottomStart,
                )
                Spacer(modifier = Modifier.width(15.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
            ) {
                HpBar(hp = myMatchPlayerVo.hp.toFloat())
                Text(
                    text = "${max(matchVo.round, 1)}/${BattleConst.MAX_ROUND}",
                    textAlign = TextAlign.Center,
                    fontFamily = DAL_MU_RI,
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    color = MongsWhite,
                    maxLines = 1,
                )
                HpBar(hp = otherMatchPlayerVo.hp.toFloat())
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
            ) {
                Spacer(modifier = Modifier.width(15.dp))
                MatchPlayer(
                    matchPlayerVo = myMatchPlayerVo,
                    effectAlignment = Alignment.TopEnd,
                )
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
private fun BattleMatchOverContent(
    navBattleMenu: () -> Unit,
    myMatchPlayerVo: MatchPlayerVo,
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
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            ) {
                if (myMatchPlayerVo.isWinner) {
                    Image(
                        painter = painterResource(R.drawable.win),
                        contentDescription = null,
                        modifier = Modifier
                            .zIndex(2f)
                            .height(35.dp)
                            .width(90.dp),
                        contentScale = ContentScale.FillBounds
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.lose),
                        contentDescription = null,
                        modifier = Modifier
                            .zIndex(2f)
                            .height(35.dp)
                            .width(90.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                if (myMatchPlayerVo.isWinner) {
                    Image(
                        painter = painterResource(R.drawable.pointlogo),
                        contentDescription = null,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                        contentScale = ContentScale.FillBounds,
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "+ 100",
                        textAlign = TextAlign.Center,
                        fontFamily = DAL_MU_RI,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = MongsWhite,
                        maxLines = 1,
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
            ) {
                BlueButton(
                    text = "배틀종료",
                    width = 100,
                    onClick = navBattleMenu
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun MatchPlayer(
    matchPlayerVo: MatchPlayerVo,
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