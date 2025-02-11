package com.mongs.wear.presentation.component.slotPick

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.mongs.wear.core.enums.MongStateCode
import com.mongs.wear.domain.management.vo.MongVo
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongResourceCode
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.common.button.BlueButton
import com.mongs.wear.presentation.component.common.charactor.Mong

@Composable
fun Slot(
    mongVo: MongVo,
    mongDetailDialogOpen: () -> Unit,
    mongGraduateDialogOpen: () -> Unit,
    mongDeleteDialogOpen: () -> Unit,
    mongPickDialogOpen: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
        ) {
            Text(
                text = mongVo.mongName,
                textAlign = TextAlign.Center,
                fontFamily = DAL_MU_RI,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                color = MongsWhite,
                maxLines = 1,
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.52f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                if (mongVo.stateCode == MongStateCode.DEAD) {
                    Image(
                        modifier = Modifier
                            .padding(bottom = 25.dp)
                            .size(130.dp),
                        painter = painterResource(R.drawable.rip),
                        contentDescription = null
                    )
                } else {
                    Mong(
                        isPng = true,
                        mong = MongResourceCode.valueOf(mongVo.mongTypeCode),
                        onClick = mongDetailDialogOpen,
                        ratio = 0.65f,
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.28f)
        ) {
            if (mongVo.stateCode == MongStateCode.GRADUATE_READY) {
                BlueButton(
                    text = "졸업",
                    height = 32,
                    width = 55,
                    onClick = mongGraduateDialogOpen,
                )
            } else {
                BlueButton(
                    text = "삭제",
                    height = 32,
                    width = 55,
                    onClick = mongDeleteDialogOpen,
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            BlueButton(
                text = "선택",
                height = 32,
                width = 55,
                onClick = mongPickDialogOpen,
                disable = mongVo.isCurrent || mongVo.stateCode in listOf(MongStateCode.DEAD, MongStateCode.DELETE, MongStateCode.GRADUATE)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
    }
}