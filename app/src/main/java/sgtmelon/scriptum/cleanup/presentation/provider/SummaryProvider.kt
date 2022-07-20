package sgtmelon.scriptum.cleanup.presentation.provider


import android.content.res.Resources
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.scriptum.infrastructure.model.key.Theme

/**
 * Provide summary/description for preference keys.
 */
// TODO tests
class SummaryProvider(private val resources: Resources) {

    fun getTheme(theme: Theme): String {
        val id = when (theme) {
            Theme.LIGHT -> R.string.pref_app_theme_light
            Theme.DARK -> R.string.pref_app_theme_dark
            Theme.SYSTEM -> R.string.pref_app_theme_system
        }

        return resources.getString(id)
    }

    fun getSort(sort: Sort): String {
        val id = when (sort) {
            Sort.CHANGE -> R.string.pref_note_sort_change
            Sort.CREATE -> R.string.pref_note_sort_create
            Sort.RANK -> R.string.pref_note_sort_rank
            Sort.COLOR -> R.string.pref_note_sort_color
        }

        return resources.getString(id)
    }

    fun getColor(color: Color): String {
        val id = when (color) {
            Color.RED -> R.string.pref_note_color_red
            Color.PURPLE -> R.string.pref_note_color_purple
            Color.INDIGO -> R.string.pref_note_color_indigo
            Color.BLUE -> R.string.pref_note_color_blue
            Color.TEAL -> R.string.pref_note_color_turquoise
            Color.GREEN -> R.string.pref_note_color_green
            Color.YELLOW -> R.string.pref_note_color_yellow
            Color.ORANGE -> R.string.pref_note_color_orange
            Color.BROWN -> R.string.pref_note_color_brown
            Color.BLUE_GREY -> R.string.pref_note_color_blue_gray
            Color.WHITE -> R.string.pref_note_color_white
        }

        return resources.getString(id)
    }

    val savePeriod: Array<String> = resources.getStringArray(R.array.pref_note_save_period)

    val repeat: Array<String> = resources.getStringArray(R.array.pref_alarm_repeat)
    val signal: Array<String> = resources.getStringArray(R.array.pref_alarm_signal)

    fun getVolume(value: Int): String {
        return resources.getString(R.string.pref_summary_alarm_volume, value)
    }
}