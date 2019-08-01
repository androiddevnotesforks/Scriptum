package sgtmelon.scriptum.repository.preference

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.media.RingtoneManager
import android.preference.PreferenceManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Signal
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.MelodyItem
import sgtmelon.scriptum.model.state.SignalState
import sgtmelon.scriptum.room.converter.IntConverter

/**
 * Repository for work with [SharedPreferences]
 *
 * @author SerjantArbuz
 */
class PreferenceRepo(private val context: Context) : IPreferenceRepo {

    private val resources: Resources = context.resources

    private val preference: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    override var firstStart: Boolean
        get() {
            val defaultValue = resources.getBoolean(R.bool.value_first_start)
            val value = preference.getBoolean(resources.getString(R.string.key_first_start), defaultValue)

            if (value) firstStart = false

            return value
        }
        set(value) = preference.edit().putBoolean(resources.getString(R.string.key_first_start), value).apply()

    @Theme override var theme: Int
        get() = preference.getInt(resources.getString(R.string.key_app_theme), resources.getInteger(R.integer.value_app_theme))
        set(value) = preference.edit().putInt(resources.getString(R.string.key_app_theme), value).apply()


    override var repeat: Int
        get() = preference.getInt(resources.getString(R.string.key_alarm_repeat), resources.getInteger(R.integer.value_alarm_repeat))
        set(value) = preference.edit().putInt(resources.getString(R.string.key_alarm_repeat), value).apply()

    override var signal: Int
        get() = preference.getInt(resources.getString(R.string.key_alarm_signal), resources.getInteger(R.integer.value_alarm_signal))
        set(value) = preference.edit().putInt(resources.getString(R.string.key_alarm_signal), value).apply()

    override val signalCheck: BooleanArray get() = IntConverter().toArray(signal, Signal.size)

    override val signalState: SignalState
        get() = signalCheck.let {
            SignalState(it[Signal.MELODY], it[Signal.VIBRATION], it[Signal.LIGHT])
        }

    override val signalSummary: String
        get() = StringBuilder().apply {
            val summary = resources.getStringArray(R.array.text_alarm_signal)
            val array = signalCheck

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

    override var melodyUri: String
        get() = melodyList.let {
            var value = preference.getString(resources.getString(R.string.key_alarm_melody), "")
                    ?: ""

            /**
             * Check uri exist
             */
            if (value.isEmpty() || !it.map { item -> item.uri }.contains(value)) {
                value = it.first().uri
                melodyUri = value
            }

            return value
        }
        set(value) = preference.edit().putString(resources.getString(R.string.key_alarm_melody), value).apply()

    override val melodyCheck: Int get() = melodyList.map { it.uri }.indexOf(melodyUri)

    override val melodyList: List<MelodyItem>
        get() = ArrayList<MelodyItem>().apply {
            /**
             * Func which fill list with all [MelodyItem] for current [RingtoneManager] type
             */
            val fillListByType = { type: Int ->
                RingtoneManager(context).apply {
                    setType(type)
                    cursor.apply {
                        while (moveToNext()) {
                            val title = getString(RingtoneManager.TITLE_COLUMN_INDEX) ?: continue
                            val uri = getString(RingtoneManager.URI_COLUMN_INDEX) ?: continue
                            val id = getString(RingtoneManager.ID_COLUMN_INDEX) ?: continue

                            add(MelodyItem(title, uri, id))
                        }
                    }.close()
                }
            }

            fillListByType(RingtoneManager.TYPE_ALARM)
            fillListByType(RingtoneManager.TYPE_RINGTONE)
        }.sortedBy { it.title }

    override var volume: Int
        get() = preference.getInt(resources.getString(R.string.key_alarm_volume), resources.getInteger(R.integer.value_alarm_volume))
        set(value) = preference.edit().putInt(resources.getString(R.string.key_alarm_volume), value).apply()

    override var volumeIncrease: Boolean
        get() = preference.getBoolean(resources.getString(R.string.key_alarm_increase), resources.getBoolean(R.bool.value_alarm_increase))
        set(value) = preference.edit().putBoolean(resources.getString(R.string.key_alarm_increase), value).apply()


    @Sort override var sort: Int
        get() = preference.getInt(resources.getString(R.string.key_note_sort), resources.getInteger(R.integer.value_note_sort))
        set(value) = preference.edit().putInt(resources.getString(R.string.key_note_sort), value).apply()

    @Color override var defaultColor: Int
        get() = preference.getInt(resources.getString(R.string.key_note_color), resources.getInteger(R.integer.value_note_color))
        set(value) = preference.edit().putInt(resources.getString(R.string.key_note_color), value).apply()

    override var pauseSaveOn: Boolean
        get() = preference.getBoolean(resources.getString(R.string.key_save_pause), resources.getBoolean(R.bool.value_save_pause))
        set(value) = preference.edit().putBoolean(resources.getString(R.string.key_save_pause), value).apply()

    override var autoSaveOn: Boolean
        get() = preference.getBoolean(resources.getString(R.string.key_save_auto), resources.getBoolean(R.bool.value_save_auto))
        set(value) = preference.edit().putBoolean(resources.getString(R.string.key_save_auto), value).apply()

    override var savePeriod: Int
        get() = preference.getInt(resources.getString(R.string.key_save_time), resources.getInteger(R.integer.value_save_time))
        set(value) = preference.edit().putInt(resources.getString(R.string.key_save_time), value).apply()

    companion object {
        fun getInstance(context: Context): IPreferenceRepo = PreferenceRepo(context)
    }

}