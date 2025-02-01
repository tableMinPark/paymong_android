package com.mongs.wear.presentation.dialog.battle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.Text
import com.mongs.wear.domain.battle.vo.MatchPlayerVo
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.common.background.BattleMatchBackground
import com.mongs.wear.presentation.component.common.background.BattleMenuBackground
import com.mongs.wear.presentation.component.common.bar.ProgressIndicator
import com.mongs.wear.presentation.component.common.button.BlueButton
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun MatchOverDialog(
    battlePayPoint: Int,
    navBattleMenu: () -> Unit,
    myMatchPlayerVo: MatchPlayerVo?,
    modifier: Modifier = Modifier,
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
                myMatchPlayerVo?.let {
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
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                myMatchPlayerVo?.let {
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
                            text = "+ $battlePayPoint",
                            textAlign = TextAlign.Center,
                            fontFamily = DAL_MU_RI,
                            fontWeight = FontWeight.Light,
                            fontSize = 16.sp,
                            color = MongsWhite,
                            maxLines = 1,
                        )
                    }
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

@Preview(showBackground = true, showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun BattlePickDialogPreview() {
    Box{
        BattleMatchBackground()
        MatchOverDialog(
            battlePayPoint = 150,
            navBattleMenu = {},
            myMatchPlayerVo = MatchPlayerVo(isWinner = true)
        )
    }
}