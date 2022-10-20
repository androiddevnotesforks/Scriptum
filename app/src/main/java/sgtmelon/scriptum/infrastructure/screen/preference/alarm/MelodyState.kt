package sgtmelon.scriptum.infrastructure.screen.preference.alarm

import sgtmelon.scriptum.infrastructure.model.item.MelodyItem

/**
 * State of loading current melody for alarm.
 */
sealed class MelodyState {

    data class Enabled(val isGroupEnabled: Boolean) : MelodyState()

    data class Loading(val isGroupEnabled: Boolean) : MelodyState()

    data class Finish(val isGroupEnabled: Boolean, val melodyItem: MelodyItem) : MelodyState()

    object Empty : MelodyState()
}