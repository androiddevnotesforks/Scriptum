package sgtmelon.scriptum.infrastructure.screen.preference.alarm

import sgtmelon.scriptum.infrastructure.model.item.MelodyItem

/**
 * State of loading current melody for alarm.
 */
sealed class MelodyState {

    class Enabled(val isGroupEnabled: Boolean) : MelodyState()

    class Loading(val isGroupEnabled: Boolean) : MelodyState()

    class Finish(val isGroupEnabled: Boolean, val melodyItem: MelodyItem) : MelodyState()

    object Empty : MelodyState()
}