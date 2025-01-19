package com.mongs.wear.data.auth.dto.request

data class CreateDeviceRequestDto(

    val deviceId: String,

    val deviceName: String,

    val appPackageName: String,

    val fcmToken: String,
)
