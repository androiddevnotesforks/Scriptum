package sgtmelon.scriptum.repository.preference

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.media.RingtoneManager
import android.preference.PreferenceManager
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.AlarmItem
import sgtmelon.scriptum.room.converter.IntConverter


/**
 * Репозиторий для работы с [SharedPreferences]
 *
 * @author SerjantArbuz
 */
class PreferenceRepo(private val context: Context) : IPreferenceRepo {

    private val resources: Resources = context.resources

    private val preference: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    override var firstStart: Boolean
        get() {
            val defaultValue = resources.getBoolean(sgtmelon.scriptum.R.bool.value_first_start)
            val value = preference.getBoolean(resources.getString(sgtmelon.scriptum.R.string.key_first_start), defaultValue)

            if (value) firstStart = false

            return value
        }
        set(value) = preference.edit().putBoolean(resources.getString(sgtmelon.scriptum.R.string.key_first_start), value).apply()

    @Theme override var theme: Int
        get() = preference.getInt(resources.getString(sgtmelon.scriptum.R.string.key_app_theme), resources.getInteger(sgtmelon.scriptum.R.integer.value_app_theme))
        set(value) = preference.edit().putInt(resources.getString(sgtmelon.scriptum.R.string.key_app_theme), value).apply()

    override var repeat: Int
        get() = preference.getInt(resources.getString(sgtmelon.scriptum.R.string.key_alarm_repeat), resources.getInteger(sgtmelon.scriptum.R.integer.value_alarm_repeat))
        set(value) = preference.edit().putInt(resources.getString(sgtmelon.scriptum.R.string.key_alarm_repeat), value).apply()

    override var signal: Int
        get() = preference.getInt(resources.getString(sgtmelon.scriptum.R.string.key_alarm_signal), resources.getInteger(sgtmelon.scriptum.R.integer.value_alarm_signal))
        set(value) = preference.edit().putInt(resources.getString(sgtmelon.scriptum.R.string.key_alarm_signal), value).apply()

    override val volumeIncrease: Boolean
        get() {
            val defaultValue = resources.getBoolean(sgtmelon.scriptum.R.bool.value_alarm_increase)
            return preference.getBoolean(resources.getString(sgtmelon.scriptum.R.string.key_alarm_increase), defaultValue)
        }

    override var sort: Int
        get() = preference.getInt(resources.getString(sgtmelon.scriptum.R.string.key_note_sort), resources.getInteger(sgtmelon.scriptum.R.integer.value_note_sort))
        set(value) = preference.edit().putInt(resources.getString(sgtmelon.scriptum.R.string.key_note_sort), value).apply()

    @Color override var defaultColor: Int
        get() = preference.getInt(resources.getString(sgtmelon.scriptum.R.string.key_note_color), resources.getInteger(sgtmelon.scriptum.R.integer.value_note_color))
        set(value) = preference.edit().putInt(resources.getString(sgtmelon.scriptum.R.string.key_note_color), value).apply()

    override val pauseSaveOn: Boolean
        get() {
            val defaultValue = resources.getBoolean(sgtmelon.scriptum.R.bool.value_save_pause)
            return preference.getBoolean(resources.getString(sgtmelon.scriptum.R.string.key_save_pause), defaultValue)
        }

    override val autoSaveOn: Boolean
        get() {
            val defaultValue = resources.getBoolean(sgtmelon.scriptum.R.bool.value_save_auto)
            return preference.getBoolean(resources.getString(sgtmelon.scriptum.R.string.key_save_auto), defaultValue)
        }

    override var savePeriod: Int
        get() = preference.getInt(resources.getString(sgtmelon.scriptum.R.string.key_save_time), resources.getInteger(sgtmelon.scriptum.R.integer.value_save_time))
        set(value) = preference.edit().putInt(resources.getString(sgtmelon.scriptum.R.string.key_save_time), value).apply()

    override fun getSignalSummary() = StringBuilder().apply {
        val summary = resources.getStringArray(sgtmelon.scriptum.R.array.text_alarm_signal)
        val array = IntConverter().toArray(signal)

        if (summary.size < array.size) return@apply

        var firstAppend = true
        array.forEachIndexed { i, bool ->
            if (bool) {
                append(if (firstAppend) {
                    firstAppend = false
                    summary[i]
                } else {
                    (", ").plus(summary[i].toLowerCase())
                })
            }
        }
    }.toString()

    override fun getData() = StringBuilder().apply {
        append("Preference:\n\n")
        append("Theme: $theme\n")
        append("Repeat: $repeat\n")
        append("Signal: $signal\n")
        append("VolumeIncrease: $volumeIncrease\n")

        append("Sort: $sort\n")
        append("DefaultColor: $defaultColor\n")
        append("PauseSave: $pauseSaveOn\n")
        append("AutoSave: $autoSaveOn\n")
        append("SaveTime: $savePeriod\n")
    }.toString()

    override fun getAlarmList() = ArrayList<AlarmItem>().apply {
        val ringtoneManager = RingtoneManager(context).apply {
            setType(RingtoneManager.TYPE_RINGTONE or RingtoneManager.TYPE_ALARM)
        }

        ringtoneManager.cursor.apply {
            while (moveToNext()) {
                val title = getString(RingtoneManager.TITLE_COLUMN_INDEX) ?: continue
                val uri = getString(RingtoneManager.URI_COLUMN_INDEX) ?: continue
                val id = getString(RingtoneManager.ID_COLUMN_INDEX) ?: continue

                add(AlarmItem(title, uri, id))
            }
        }.close()
    }

    companion object {
        fun getInstance(context: Context): IPreferenceRepo = PreferenceRepo(context)

        const val SIGNAL_ARRAY_SIZE = 3
    }

}