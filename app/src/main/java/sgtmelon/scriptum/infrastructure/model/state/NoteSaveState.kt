package sgtmelon.scriptum.infrastructure.model.state

import sgtmelon.scriptum.infrastructure.model.key.SavePeriod

data class NoteSaveState(
    val isPauseSaveOn: Boolean,
    val isAutoSaveOn: Boolean,
    val savePeriod: SavePeriod
)