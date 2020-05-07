package sgtmelon.scriptum.data.repository.preference

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import sgtmelon.scriptum.data.provider.PreferenceProvider
import sgtmelon.scriptum.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.domain.model.annotation.*

/**
 * Repository for work with [SharedPreferences]
 */
class PreferenceRepo(context: Context) : IPreferenceRepo {

    private val key = PreferenceProvider.Key(context)
    private val def = PreferenceProvider.Def(context)

    private val preferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    private fun edit(func: SharedPreferences.Editor.() -> Unit) =
            preferences.edit().apply(func).apply()

    override var firstStart: Boolean
        get() = preferences.getBoolean(key.FIRST_START, def.FIRST_START)
        set(value) = edit { putBoolean(key.FIRST_START, value) }

    @Theme override var theme: Int
        get() = preferences.getInt(key.THEME, def.THEME)
        set(value) = edit { putInt(key.THEME, value) }


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


    @Repeat override var repeat: Int
        get() = preferences.getInt(key.REPEAT, def.REPEAT)
        set(value) = edit { putInt(key.REPEAT, value) }

    @Signal override var signal: Int
        get() = preferences.getInt(key.SIGNAL, def.SIGNAL)
        set(value) = edit { putInt(key.SIGNAL, value) }

    /**
     * Access only from [SignalInteractor.getMelodyUri]/[SignalInteractor.setMelodyUri].
     */
    override var melodyUri: String
        get() = preferences.getString(key.MELODY_URI, def.MELODY_URI) ?: def.MELODY_URI
        set(value) = edit { putString(key.MELODY_URI, value) }

    override var volume: Int
        get() = preferences.getInt(key.VOLUME, def.VOLUME)
        set(value) = edit { putInt(key.VOLUME, value) }

    override var volumeIncrease: Boolean
        get() = preferences.getBoolean(key.VOLUME_INCREASE, def.VOLUME_INCREASE)
        set(value) = edit { putBoolean(key.VOLUME_INCREASE, value) }


    override fun clear() = edit { clear() }

}