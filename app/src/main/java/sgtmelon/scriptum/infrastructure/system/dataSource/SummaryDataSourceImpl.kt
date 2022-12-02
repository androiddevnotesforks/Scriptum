package sgtmelon.scriptum.infrastructure.system.dataSource


import android.content.res.Resources
import java.util.Locale
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.infrastructure.model.exception.DifferentSizeException
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.utils.extensions.record

/**
 * Provide summary/description for preference keys.
 */
class SummaryDataSourceImpl(private val resources: Resources) : SummaryDataSource {

    override fun getTheme(theme: Theme): String {
        val id = when (theme) {
            Theme.LIGHT -> R.string.pref_theme_light
            Theme.DARK -> R.string.pref_theme_dark
            Theme.SYSTEM -> R.string.pref_theme_system
        }

        return resources.getString(id)
    }

    override fun getSort(sort: Sort): String {
        val id = when (sort) {
            Sort.CHANGE -> R.string.pref_sort_change
            Sort.CREATE -> R.string.pref_sort_create
            Sort.RANK -> R.string.pref_sort_rank
            Sort.COLOR -> R.string.pref_sort_color
        }

        return resources.getString(id)
    }

    override fun getColor(color: Color): String {
        val id = when (color) {
            Color.RED -> R.string.pref_color_red
            Color.PURPLE -> R.string.pref_color_purple
            Color.INDIGO -> R.string.pref_color_indigo
            Color.BLUE -> R.string.pref_color_blue
            Color.TEAL -> R.string.pref_color_teal
            Color.GREEN -> R.string.pref_color_green
            Color.YELLOW -> R.string.pref_color_yellow
            Color.ORANGE -> R.string.pref_color_orange
            Color.BROWN -> R.string.pref_color_brown
            Color.BLUE_GREY -> R.string.pref_color_blue_gray
            Color.WHITE -> R.string.pref_color_white
        }

        return resources.getString(id)
    }

    override fun getSavePeriod(savePeriod: SavePeriod): String {
        val id = when (savePeriod) {
            SavePeriod.MIN_1 -> R.string.pref_save_period_1
            SavePeriod.MIN_3 -> R.string.pref_save_period_3
            SavePeriod.MIN_7 -> R.string.pref_save_period_7
        }

        return resources.getString(id)
    }

    override fun getRepeat(repeat: Repeat): String {
        val id = when (repeat) {
            Repeat.MIN_10 -> R.string.pref_repeat_0
            Repeat.MIN_30 -> R.string.pref_repeat_1
            Repeat.MIN_60 -> R.string.pref_repeat_2
            Repeat.MIN_180 -> R.string.pref_repeat_3
            Repeat.MIN_1440 -> R.string.pref_repeat_4
        }

        return resources.getString(id)
    }

    override fun getSignal(valueArray: BooleanArray): String {
        val summaryArray = resources.getStringArray(R.array.pref_signal)

        if (summaryArray.size != valueArray.size) {
            DifferentSizeException(valueArray.size, summaryArray.size).record()
            return ""
        }

        return StringBuilder().apply {
            var firstAppend = true

            for ((i, checked) in valueArray.withIndex()) {
                if (!checked) continue

                val text = if (firstAppend) {
                    firstAppend = false
                    summaryArray[i]
                } else {
                    SIGNAL_DIVIDER.plus(summaryArray[i].lowercase(Locale.getDefault()))
                }

                append(text)
            }
        }.toString()
    }

    override fun getVolume(value: Int): String {
        return resources.getString(R.string.pref_summary_alarm_volume, value)
    }

    companion object {
        const val SIGNAL_DIVIDER = ", "
    }
}