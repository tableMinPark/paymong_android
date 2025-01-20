package com.mongs.wear.domain.auth.usecase

import com.mongs.wear.core.errors.DataErrorCode
import com.mongs.wear.core.exception.ErrorException
import com.mongs.wear.domain.auth.exception.CreateDeviceException
import com.mongs.wear.domain.auth.exception.LoginException
import com.mongs.wear.domain.auth.exception.NeedJoinException
import com.mongs.wear.domain.auth.exception.NeedUpdateAppException
import com.mongs.wear.domain.auth.exception.NotExistsEmailException
import com.mongs.wear.domain.auth.exception.NotExistsGoogleAccountIdException
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.domain.global.usecase.BaseParamUseCase
import com.mongs.wear.domain.player.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val deviceRepository: DeviceRepository,
    private val playerRepository: PlayerRepository,
    private val authRepository: AuthRepository,
) : BaseParamUseCase<LoginUseCase.Param, Unit>() {

    /**
     * 로그인 UseCase
     * 1. 기기 등록
     * 2. 로그인
     * 3. 브로커 연결
     * 4. 플레이어 정보 등록
     * 5. 네트워크 플래그 true
     */
    override suspend fun execute(param: Param) {

        withContext(Dispatchers.IO) {

            if (param.email.isNullOrEmpty()) throw NotExistsEmailException()

            if (param.googleAccountId.isNullOrEmpty()) throw NotExistsGoogleAccountIdException()

            // 기기 등록
            authRepository.createDevice(
                deviceId = deviceRepository.getDeviceId(),
                fcmToken = param.fcmToken,
            )

            // 로그인
            val accountId = authRepository.login(
                deviceId = deviceRepository.getDeviceId(),
                email = param.email,
                googleAccountId = param.googleAccountId
            )

            // 브로커 연결
            mqttClient.connect()

            // 브로커 연결 여부 확인
            if (mqttClient.isConnected()) {
                // 플레이어 정보 등록
                playerRepository.createPlayer()
                // 플레이어 정보 구독
                mqttClient.subPlayer(accountId = accountId)
                // 네트워크 flag 설정
                deviceRepository.setNetwork(network = true)
            } else {
                throw LoginException()
            }
        }
    }

    data class Param(

        val googleAccountId: String?,

        val email: String?,

        val fcmToken: String,
    )

    override fun handleException(exception: ErrorException) {
        super.handleException(exception)

        when(exception.code) {

            DataErrorCode.DATA_AUTH_NEED_JOIN -> throw NeedJoinException()

            DataErrorCode.DATA_AUTH_NEED_UPDATE_APP -> throw NeedUpdateAppException()

            DataErrorCode.DATA_AUTH_CREATE_DEVICE -> throw CreateDeviceException()

            else -> throw LoginException()
        }
    }
}