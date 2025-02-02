package com.mongs.wear.data.global.client

data class MqttState(
    var connectPending: Boolean = false,
    var broker: MqttConnectedStateCode = MqttConnectedStateCode.DISCONNECT,
    var manager: MqttSubscribeStateCode = MqttSubscribeStateCode.DIS_SUB,
    var player: MqttSubscribeStateCode = MqttSubscribeStateCode.DIS_SUB,
    var searchMatch: MqttSubscribeStateCode = MqttSubscribeStateCode.DIS_SUB,
    var battleMatch: MqttSubscribeStateCode = MqttSubscribeStateCode.DIS_SUB,
    var managerTopic: String = "",
    var playerTopic: String = "",
    var searchMatchTopic: String = "",
    var battleMatchTopic: String = "",

) {
    fun reset() {
        this.connectPending = false
        this.broker = MqttConnectedStateCode.DISCONNECT
        this.manager = MqttSubscribeStateCode.DIS_SUB
        this.player = MqttSubscribeStateCode.DIS_SUB
        this.searchMatch = MqttSubscribeStateCode.DIS_SUB
        this.battleMatch = MqttSubscribeStateCode.DIS_SUB
        this.managerTopic = ""
        this.playerTopic = ""
        this.searchMatchTopic = ""
        this.battleMatchTopic = ""
    }

    fun isConnect(): Boolean {
        return this.broker == MqttConnectedStateCode.CONNECT
    }

    fun isDisConnect(): Boolean {
        return this.broker == MqttConnectedStateCode.DISCONNECT
    }

    fun isPauseDisConnect(): Boolean {
        return this.broker == MqttConnectedStateCode.PAUSE_DISCONNECT
    }

    enum class MqttConnectedStateCode {
        CONNECT, PAUSE_DISCONNECT, DISCONNECT,
    }

    enum class MqttSubscribeStateCode {
        SUB, DIS_SUB,
    }
}
