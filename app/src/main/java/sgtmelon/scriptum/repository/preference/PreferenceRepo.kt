package sgtmelon.scriptum.repository.preference

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.media.RingtoneManager
import android.preference.PreferenceManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.*
import sgtmelon.scriptum.model.item.MelodyItem
import sgtmelon.scriptum.model.state.SignalState
import sgtmelon.scriptum.provider.PreferenceProvider
import sgtmelon.scriptum.room.converter.IntConverter

/**
 * Repository for work with [SharedPreferences]
 *
 * @author SerjantArbuz
 */
@Suppress("PrivatePropertyName")
class PreferenceRepo(private val context: Context) : IPreferenceRepo {

    private val resources: Resources = context.resources

    private val key = PreferenceProvider.Key(context)
    private val def = PreferenceProvider.Def(context)

    private val preferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    private fun edit(func: SharedPreferences.Editor.() -> Unit) =
            preferences.edit().apply(func).apply()

    override var firstStart: Boolean
        get() = preferences.getBoolean(key.FIRST_START, def.FIRST_START).apply {
            if (this) firstStart = false
        }
        set(value) = edit { putBoolean(key.FIRST_START, value) }

    @Theme override var theme: Int
        get() = preferences.getInt(key.THEME, def.THEME)
        set(value) = edit { putInt(key.THEME, value) }

    @Repeat override var repeat: Int
        get() = preferences.getInt(key.REPEAT, def.REPEAT)
        set(value) = edit { putInt(key.REPEAT, value) }

    override var signal: Int
        get() = preferences.getInt(key.SIGNAL, def.SIGNAL)
        set(value) = edit { putInt(key.SIGNAL, value) }

    override val signalCheck: BooleanArray get() = IntConverter().toArray(signal, Signal.digitCount)

    override val signalState: SignalState get() = SignalState(signalCheck)

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
            var value = preferences.getString(key.MELODY_URI, def.MELODY_URI) ?: def.MELODY_URI

            /**
             * Check uri exist
             */
            if (value.isEmpty() || !it.map { item -> item.uri }.contains(value)) {
                value = it.first().uri
                melodyUri = value
            }

            return value
        }
        set(value) = edit { putString(key.MELODY_URI, value) }

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
        get() = preferences.getInt(key.VOLUME, def.VOLUME)
        set(value) = edit { putInt(key.VOLUME, value) }

    override var volumeIncrease: Boolean
        get() = preferences.getBoolean(key.VOLUME_INCREASE, def.VOLUME_INCREASE)
        set(value) = edit { putBoolean(key.VOLUME_INCREASE, value) }

    @Sort override var sort: Int
        get() = preferences.getInt(key.SORT, def.SORT)
        set(value) = edit { putInt(key.SORT, value) }

    @Color override var defaultColor: Int
        get() = preferences.getInt(key.DEFAULT_COLOR, def.DEFAULT_COLOR)
        set(value) = edit { putInt(key.DEFAULT_COLOR, value) }

    override var pauseSaveOn: Boolean
        get() = preferences.getBoolean(key.PAUSE_SAVE_ON, def.PAUSE_SAVE_ON)
        set(value) = edit { putBoolean(key.PAUSE_SAVE_ON, value) }

    override var autoSaveOn: Boolean
        get() = preferences.getBoolean(key.AUTO_SAVE_ON, def.AUTO_SAVE_ON)
        set(value) = edit { putBoolean(key.AUTO_SAVE_ON, value) }

    override var savePeriod: Int
        get() = preferences.getInt(key.SAVE_PERIOD, def.SAVE_PERIOD)
        set(value) = edit { putInt(key.SAVE_PERIOD, value) }

    companion object {
        fun getInstance(context: Context): IPreferenceRepo = PreferenceRepo(context)
    }

}