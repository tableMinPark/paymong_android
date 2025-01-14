//package com.mongs.wear.presentation.component.common.textbox
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Devices
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.zIndex
//import androidx.wear.compose.material.Text
//import com.mongs.wear.presentation.R
//import com.mongs.wear.presentation.assets.DAL_MU_RI
//import com.mongs.wear.presentation.assets.MongsDarkGreen
//
//@Composable
//fun ScoreBox(
//    height: Int = 30,
//    width: Int = 80,
//    score: Int = 0,
//    modifier: Modifier = Modifier,
//) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = modifier
//            .height(height.dp)
//            .width(width.dp),
//    ) {
//        Image(
//            painter = painterResource(R.drawable.pointbackground),
//            contentDescription = null,
//            modifier = Modifier
//                .matchParentSize()
//                .zIndex(1.1f),
//            contentScale = ContentScale.FillBounds,
//        )
//
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.zIndex(1.2f).padding(start = 10.dp, end = 10.dp)
//        ) {
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier.weight(0.4f),
//            ) {
//                Image(
//                    painter = painterResource(R.drawable.runner),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .height(22.dp)
//                        .width(22.dp),
//                    contentScale = ContentScale.FillBounds,
//                )
//            }
//
//            Text(
//                text = "$score",
//                textAlign = TextAlign.Center,
//                fontFamily = DAL_MU_RI,
//                fontWeight = FontWeight.Light,
//                fontSize = 14.sp,
//                color = MongsDarkGreen,
//                maxLines = 1,
//                modifier = Modifier.weight(0.6f)
//            )
//        }
//    }
//}
//
//
//@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
//@Composable
//private fun Preview() {
//    ScoreBox()
//}
//
//@Preview(device = Devices.WEAR_OS_SMALL_ROUND)
//@Composable
//private fun LongPreview() {
//    ScoreBox(width = 120)
//}