package com.mongs.wear.data.manager.consumer

import com.google.gson.Gson
import com.mongs.wear.data.manager.dto.response.MongBasicDto
import com.mongs.wear.data.manager.dto.response.MongStateDto
import com.mongs.wear.data.manager.dto.response.MongStatusDto
import com.mongs.wear.data.manager.resolver.ManagementObserveResolver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManagementConsumer  @Inject constructor(
    private val observeResolver: ManagementObserveResolver,
    private val gson: Gson,
) {

    companion object {
        private const val MANAGER_MANAGEMENT_OBSERVE_MONG_BASIC = "MANAGER-MANAGEMENT-200"
        private const val MANAGER_MANAGEMENT_OBSERVE_MONG_STATE = "MANAGER-MANAGEMENT-201"
        private const val MANAGER_MANAGEMENT_OBSERVE_MONG_STATUS = "MANAGER-MANAGEMENT-202"
    }

    fun messageArrived(code: String, resultJson: String) {

        when (code) {
            MANAGER_MANAGEMENT_OBSERVE_MONG_BASIC ->
                observeResolver.updateMongBasic(
                    mongBasicDto = gson.fromJson(
                        resultJson,
                        MongBasicDto::class.java
                    )
                )
            MANAGER_MANAGEMENT_OBSERVE_MONG_STATE ->
                observeResolver.updateMongState(
                    mongStateDto =  gson.fromJson(
                        resultJson,
                        MongStateDto::class.java
                    )
                )

            MANAGER_MANAGEMENT_OBSERVE_MONG_STATUS ->
                observeResolver.updateMongStatus(
                    mongStatusDto =  gson.fromJson(
                        resultJson,
                        MongStatusDto::class.java
                    )
                )

            else -> {}
        }
    }
}