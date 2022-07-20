package sgtmelon.scriptum.cleanup.presentation.provider


import android.content.res.Resources
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.scriptum.infrastructure.model.key.Theme

/**
 * Provide summary/description for preference keys.
 */
// TODO tests
class SummaryProvider(private val resources: Resources) {

    fun getTheme(theme: Theme) = resources.getString(when (theme) {
        Theme.LIGHT -> R.string.pref_text_app_theme_light
        Theme.DARK -> R.string.pref_text_app_theme_dark
        Theme.SYSTEM -> R.string.pref_text_app_theme_system
    })

    fun getSort(sort: Sort) = resources.getString(when (sort) {
        Sort.CHANGE -> R.string.pref_text_note_sort_change
        Sort.CREATE -> R.string.pref_text_note_sort_create
        Sort.RANK -> R.string.pref_text_note_sort_rank
        Sort.COLOR -> R.string.pref_text_note_sort_color
    })

    @Deprecated("Use getSort")
    val sort: Array<String> = resources.getStringArray(R.array.pref_text_note_sort)
    val color: Array<String> = resources.getStringArray(R.array.pref_text_note_color)
    val savePeriod: Array<String> = resources.getStringArray(R.array.pref_text_note_save_period)

    val repeat: Array<String> = resources.getStringArray(R.array.pref_text_alarm_repeat)
    val signal: Array<String> = resources.getStringArray(R.array.pref_text_alarm_signal)

    fun getVolume(value: Int): String {
        return resources.getString(R.string.pref_summary_alarm_volume, value)
    }
}