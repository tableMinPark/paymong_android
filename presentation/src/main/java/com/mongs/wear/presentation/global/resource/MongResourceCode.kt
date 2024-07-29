package com.mongs.wear.presentation.global.resource

import com.mongs.wear.presentation.R

enum class MongResourceCode(
    val pngCode: Int,
    val gifCode: Int,
    val code: String,
    val yOffset: Int,
    val xOffset: Int,
    val hasExpression: Boolean,
    val isEgg: Boolean,
) {
    CH000(R.drawable.ch000, R.drawable.ch000g, "CH000", 0, 0, false, true),
    CH001(R.drawable.ch001, R.drawable.ch001g, "CH001", 0, 0, false, true),
    CH002(R.drawable.ch002, R.drawable.ch002g, "CH002", 0, 0, false, true),
    CH003(R.drawable.ch003, R.drawable.ch003g, "CH003", 0, 0, false, true),
    CH004(R.drawable.ch004, R.drawable.ch004g, "CH004", 0, 0, false, true),
    CH005(R.drawable.ch005, R.drawable.ch005g, "CH005", 0, 0, false, true),
    CH100(R.drawable.ch100, R.drawable.ch100g, "CH100", -4, 0, true, false),
    CH101(R.drawable.ch101, R.drawable.ch101g, "CH101", -2, 0, true, false),
    CH102(R.drawable.ch102, R.drawable.ch102g, "CH102", -4, 0, true, false),
    CH200(R.drawable.ch200, R.drawable.ch200g, "CH200", -16, 0, true, false),
    CH210(R.drawable.ch210, R.drawable.ch210g, "CH210", -16, 0, true, false),
    CH220(R.drawable.ch220, R.drawable.ch220g, "CH220", -16, 0, true, false),
    CH230(R.drawable.ch230, R.drawable.ch230g, "CH230", -16, 0, true, false),
    CH201(R.drawable.ch201, R.drawable.ch201g, "CH201", -13, -11, true, false),
    CH211(R.drawable.ch211, R.drawable.ch211g, "CH211", -13, -11, true, false),
    CH221(R.drawable.ch221, R.drawable.ch221g, "CH221", -13, -11, true, false),
    CH231(R.drawable.ch231, R.drawable.ch231g, "CH231", -13, -11, true, false),
    CH202(R.drawable.ch202, R.drawable.ch202g, "CH202", -16, 0, true, false),
    CH212(R.drawable.ch212, R.drawable.ch212g, "CH212", -16, 0, true, false),
    CH222(R.drawable.ch222, R.drawable.ch222g, "CH222", -16, 0, true, false),
    CH232(R.drawable.ch232, R.drawable.ch232g, "CH232", -16, 0, true, false),
    CH203(R.drawable.ch203, R.drawable.ch203, "CH203", 0, 0, false, false),
    CH300(R.drawable.ch300, R.drawable.ch300g, "CH300", -15, 0, true, false),
    CH310(R.drawable.ch310, R.drawable.ch310g, "CH310", -15, 0, true, false),
    CH320(R.drawable.ch320, R.drawable.ch320g, "CH320", -15, 0, true, false),
    CH330(R.drawable.ch330, R.drawable.ch330g, "CH330", -15, 0, true, false),
    CH301(R.drawable.ch301, R.drawable.ch301g, "CH301", -13, -23, true, false),
    CH311(R.drawable.ch311, R.drawable.ch311g, "CH311", -13, -23, true, false),
    CH321(R.drawable.ch321, R.drawable.ch321g, "CH321", -13, -23, true, false),
    CH331(R.drawable.ch331, R.drawable.ch331g, "CH331", -13, -23, true, false),
    CH302(R.drawable.ch302, R.drawable.ch302g, "CH302", -8, 0, true, false),
    CH312(R.drawable.ch312, R.drawable.ch312g, "CH312", -8, 0, true, false),
    CH322(R.drawable.ch322, R.drawable.ch322g, "CH322", -8, 0, true, false),
    CH332(R.drawable.ch332, R.drawable.ch332g, "CH332", -8, 0, true, false),
    CH303(R.drawable.ch303, R.drawable.ch303, "CH303", 0, 0, false, false),
    CH444(R.drawable.none, R.drawable.none, "CH444", 0, 0, false, false)
}