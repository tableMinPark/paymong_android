package com.mongs.wear.domain.auth.usecase

import com.mongs.wear.core.exception.data.CreateDeviceException
import com.mongs.wear.core.exception.data.LoginException
import com.mongs.wear.core.exception.data.NeedJoinException
import com.mongs.wear.core.exception.data.NeedUpdateAppException
import com.mongs.wear.core.exception.global.DataException
import com.mongs.wear.core.exception.usecase.CreateDeviceUseCaseException
import com.mongs.wear.core.exception.usecase.LoginUseCaseException
import com.mongs.wear.core.exception.usecase.NeedJoinUseCaseException
import com.mongs.wear.core.exception.usecase.NeedUpdateAppUseCaseException
import com.mongs.wear.domain.auth.repository.AuthRepository
import com.mongs.wear.domain.device.repository.DeviceRepository
import com.mongs.wear.domain.global.client.MqttClient
import com.mongs.wear.core.usecase.BaseParamUseCase
import com.mongs.wear.domain.management.repository.ManagementRepository
import com.mongs.wear.domain.player.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val mqttClient: MqttClient,
    private val deviceRepository: DeviceRepository,
    private val managementRepository: ManagementRepository,
    private val playerRepository: PlayerRepository,
    private val authRepository: AuthRepository,
) : BaseParamUseCase<LoginUseCase.Param, Unit>() {

    /**
     * 로그인 UseCase
     * @throws CreateDeviceException 기기 등록 실패
     * @throws NeedUpdateAppUseCaseException 앱 업데이트 필요
     * @throws NeedJoinUseCaseException 회원 가입 필요
     * @throws LoginException 로그인 실패
     */
    override suspend fun execute(param: Param) {

        withContext(Dispatchers.IO) {
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

            // 브로커 연결 여부 확인
            if (mqttClient.isConnected()) {
                // 플레이어 정보 등록
                playerRepository.createPlayer()
                // 플레이어 정보 구독
                mqttClient.subPlayer(accountId = accountId)
                // 네트워크 flag 설정
                deviceRepository.setNetwork(network = true)
                // 몽 정보 전체 동기화
                managementRepository.updateMongs()
            } else {
                throw LoginUseCaseException()
            }
        }
    }

    data class Param(

        val googleAccountId: String,

        val email: String,

        val fcmToken: String,
    )

    override fun handleException(exception: DataException) {
        super.handleException(exception)

        when (exception) {
            is NeedUpdateAppException -> throw NeedUpdateAppUseCaseException()

            is NeedJoinException -> throw NeedJoinUseCaseException()

            is CreateDeviceException -> throw CreateDeviceUseCaseException()

            is LoginException -> throw LoginUseCaseException()

            else -> throw LoginUseCaseException()
        }
    }
}