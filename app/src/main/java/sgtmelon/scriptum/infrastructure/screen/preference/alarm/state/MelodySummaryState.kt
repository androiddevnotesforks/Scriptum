package sgtmelon.scriptum.infrastructure.screen.preference.alarm.state

/**
 * State of loading current melody for alarm.
 */
sealed class MelodySummaryState {

    object Loading : MelodySummaryState()

    data class Finish(val title: String) : MelodySummaryState()

    object Empty : MelodySummaryState()
}