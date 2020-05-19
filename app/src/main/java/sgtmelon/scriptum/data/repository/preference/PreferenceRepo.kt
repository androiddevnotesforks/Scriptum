package sgtmelon.scriptum.data.repository.preference

import android.content.SharedPreferences
import sgtmelon.scriptum.data.provider.PreferenceProvider
import sgtmelon.scriptum.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.domain.model.annotation.*

/**
 * Repository for work with [SharedPreferences]
 */
class PreferenceRepo(
        private val key: PreferenceProvider.Key,
        private val def: PreferenceProvider.Def,
        private val preferences: SharedPreferences?
) : IPreferenceRepo {

    override var firstStart: Boolean?
        get() = get(key.FIRST_START, def.FIRST_START) { key, def -> getBoolean(key, def) }
        set(value) = set(key.FIRST_START, value) { key, set -> putBoolean(key, set) }

    @Theme override var theme: Int?
        get() = get(key.THEME, def.THEME) { key, def -> getInt(key, def) }
        set(value) = set(key.THEME, value) { key, set -> putInt(key, set) }


    @Sort override var sort: Int?
        get() = get(key.SORT, def.SORT) { key, def -> getInt(key, def) }
        set(value) = set(key.SORT, value) { key, set -> putInt(key, set) }

    @Color override var defaultColor: Int?
        get() = get(key.DEFAULT_COLOR, def.DEFAULT_COLOR) { key, def -> getInt(key, def) }
        set(value) = set(key.DEFAULT_COLOR, value) { key, set -> putInt(key, set) }

    override var pauseSaveOn: Boolean?
        get() = get(key.PAUSE_SAVE_ON, def.PAUSE_SAVE_ON) { key, def -> getBoolean(key, def) }
        set(value) = set(key.PAUSE_SAVE_ON, value) { key, set -> putBoolean(key, set) }

    override var autoSaveOn: Boolean?
        get() = get(key.AUTO_SAVE_ON, def.AUTO_SAVE_ON) { key, def -> getBoolean(key, def) }
        set(value) = set(key.AUTO_SAVE_ON, value) { key, set -> putBoolean(key, set) }

    override var savePeriod: Int?
        get() = get(key.SAVE_PERIOD, def.SAVE_PERIOD) { key, def -> getInt(key, def) }
        set(value) = set(key.SAVE_PERIOD, value) { key, set -> putInt(key, set) }


    @Repeat override var repeat: Int?
        get() = get(key.REPEAT, def.REPEAT) { key, def -> getInt(key, def) }
        set(value) = set(key.REPEAT, value) { key, set -> putInt(key, set) }

    @Signal override var signal: Int?
        get() = get(key.SIGNAL, def.SIGNAL) { key, def -> getInt(key, def) }
        set(value) = set(key.SIGNAL, value) { key, set -> putInt(key, set) }

    /**
     * Access only from [SignalInteractor.getMelodyUri]/[SignalInteractor.setMelodyUri].
     */
    override var melodyUri: String?
        get() = get(key.MELODY_URI, def.MELODY_URI) { key, def -> getString(key, def) ?: def }
        set(value) = set(key.MELODY_URI, value) { key, set -> putString(key, set) }

    override var volume: Int?
        get() = get(key.VOLUME, def.VOLUME) { key, def -> getInt(key, def) }
        set(value) = set(key.VOLUME, value) { key, set -> putInt(key, set) }

    override var volumeIncrease: Boolean?
        get() = get(key.VOLUME_INCREASE, def.VOLUME_INCREASE) { key, def -> getBoolean(key, def) }
        set(value) = set(key.VOLUME_INCREASE, value) { key, set -> putBoolean(key, set) }


    override fun clear() = edit { clear() }


    private fun <T> get(key: String?, def: T?,
                        func: SharedPreferences.(key: String, def: T) -> T): T? {
        if (key == null || def == null) return null

        return preferences?.let { it.func(key, def) }
    }

    private fun <T> set(key: String?, set: T?,
                        func: SharedPreferences.Editor.(key: String, set: T) -> Unit) {
        if (key == null || set == null) return

        preferences?.edit()?.apply { func(key, set) }?.apply()
    }

    private fun edit(func: SharedPreferences.Editor.() -> Unit) {
        preferences?.edit()?.apply(func)?.apply()
    }

}