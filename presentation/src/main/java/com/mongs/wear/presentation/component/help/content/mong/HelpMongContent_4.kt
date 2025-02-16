package com.mongs.wear.presentation.component.help.content.mong

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.mongs.wear.presentation.R
import com.mongs.wear.presentation.assets.DAL_MU_RI
import com.mongs.wear.presentation.assets.MongsWhite
import com.mongs.wear.presentation.component.common.button.CircleImageButton
import com.mongs.wear.presentation.global.constValue.HelpConst

@Composable
fun HelpMongContent_4() {
    
    Column (
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        Spacer(modifier = Modifier.height(HelpConst.CONTENT_TOP_PADDING.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
        ) {
            CircleImageButton(
                icon = R.drawable.btn_icon_sleep,
                border = R.drawable.btn_border_blue,
                onClick = {}
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
        ) {
            Text(
                text = "클릭해 수면/기상",
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
        ) {
            Text(
                text = "수면 상태에는",
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
        ) {
            Image(
                painter = painterResource(R.drawable.icon_healthy),
                contentDescription = null,
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp),
                contentScale = ContentScale.FillBounds,
            )

            Spacer(modifier = Modifier.width(HelpConst.CONTENT_IMAGE_PADDING.dp))

            Image(
                painter = painterResource(R.drawable.icon_fatigue),
                contentDescription = null,
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp),
                contentScale = ContentScale.FillBounds,
            )

            Spacer(modifier = Modifier.width(HelpConst.CONTENT_IMAGE_PADDING.dp))

            Text(
                text = "이 증가해요",
                textAlign = TextAlign.Center,
                fontFamily = DAL_MU_RI,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
                color = MongsWhite,
                maxLines = 1,
            )
        }
        Spacer(modifier = Modifier.height(HelpConst.CONTENT_BOTTOM_PADDING.dp))
    }
}