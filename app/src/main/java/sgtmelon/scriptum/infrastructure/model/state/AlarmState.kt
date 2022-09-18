package sgtmelon.scriptum.infrastructure.model.state

data class AlarmState(
    val signalState: SignalState,
    val volumePercent: Int,
    val isVolumeIncrease: Boolean
)