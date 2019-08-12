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
        get() = preferences.getBoolean(key.firstStart, def.firstStart).apply {
            if (this) firstStart = false
        }
        set(value) = edit { putBoolean(key.firstStart, value) }

    @Theme override var theme: Int
        get() = preferences.getInt(key.theme, def.theme)
        set(value) = edit { putInt(key.theme, value) }

    override var repeat: Int
        get() = preferences.getInt(key.repeat, def.repeat)
        set(value) = edit { putInt(key.repeat, value) }

    override var signal: Int
        get() = preferences.getInt(key.signal, def.signal)
        set(value) = edit { putInt(key.signal, value) }

    override val signalCheck: BooleanArray get() = IntConverter().toArray(signal, Signal.size)

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
            var value = preferences.getString(key.melodyUri, def.melodyUri) ?: def.melodyUri

            /**
             * Check uri exist
             */
            if (value.isEmpty() || !it.map { item -> item.uri }.contains(value)) {
                value = it.first().uri
                melodyUri = value
            }

            return value
        }
        set(value) = edit { putString(key.melodyUri, value) }

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
        get() = preferences.getInt(key.volume, def.volume)
        set(value) = edit { putInt(key.volume, value) }

    override var volumeIncrease: Boolean
        get() = preferences.getBoolean(key.volumeIncrease, def.volumeIncrease)
        set(value) = edit { putBoolean(key.volumeIncrease, value) }

    @Sort override var sort: Int
        get() = preferences.getInt(key.sort, def.sort)
        set(value) = edit { putInt(key.sort, value) }

    @Color override var defaultColor: Int
        get() = preferences.getInt(key.defaultColor, def.defaultColor)
        set(value) = edit { putInt(key.defaultColor, value) }

    override var pauseSaveOn: Boolean
        get() = preferences.getBoolean(key.pauseSaveOn, def.pauseSaveOn)
        set(value) = edit { putBoolean(key.pauseSaveOn, value) }

    override var autoSaveOn: Boolean
        get() = preferences.getBoolean(key.autoSaveOn, def.autoSaveOn)
        set(value) = edit { putBoolean(key.autoSaveOn, value) }

    override var savePeriod: Int
        get() = preferences.getInt(key.savePeriod, def.savePeriod)
        set(value) = edit { putInt(key.savePeriod, value) }

    companion object {
        fun getInstance(context: Context): IPreferenceRepo = PreferenceRepo(context)
    }

}