package sgtmelon.scriptum.infrastructure.model.state

import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod

data class NoteSaveState(
    val isPauseSaveOn: Boolean,
    val isAutoSaveOn: Boolean,
    val savePeriod: SavePeriod
)