package sgtmelon.scriptum.infrastructure.preferences

import android.content.SharedPreferences
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.*
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider

/**
 * Repository for work with [SharedPreferences]
 */
class PreferencesImpl(
    private val key: PreferencesKeyProvider,
    private val def: PreferencesDefProvider,
    private val preferences: SharedPreferences
) : Preferences {

    override var isFirstStart: Boolean
        get() = preferences.getBoolean(key.isFirstStart, def.isFirstStart)
        set(value) = edit { putBoolean(key.isFirstStart, value) }

    @Theme override var theme: Int
        get() = preferences.getInt(key.theme, def.theme)
        set(value) = edit { putInt(key.theme, value) }


    override var isBackupSkipImports: Boolean
        get() = preferences.getBoolean(key.isBackupSkipImports, def.isBackupSkipImports)
        set(value) = edit { putBoolean(key.isBackupSkipImports, value) }


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

    @SavePeriod override var savePeriod: Int
        get() = preferences.getInt(key.savePeriod, def.savePeriod)
        set(value) = edit { putInt(key.savePeriod, value) }


    @Repeat override var repeat: Int
        get() = preferences.getInt(key.repeat, def.repeat)
        set(value) = edit { putInt(key.repeat, value) }

    override var signal: Int
        get() = preferences.getInt(key.signal, def.signal)
        set(value) = edit { putInt(key.signal, value) }

    /**
     * Access only from [SignalInteractor.getMelodyUri]/[SignalInteractor.setMelodyUri].
     */
    override var melodyUri: String
        get() = preferences.getString(key.melodyUri, def.melodyUri) ?: def.melodyUri
        set(value) = edit { putString(key.melodyUri, value) }

    override var volume: Int
        get() = preferences.getInt(key.volume, def.volume)
        set(value) = edit { putInt(key.volume, value) }

    override var volumeIncrease: Boolean
        get() = preferences.getBoolean(key.volumeIncrease, def.volumeIncrease)
        set(value) = edit { putBoolean(key.volumeIncrease, value) }


    override var isDeveloper: Boolean
        get() = preferences.getBoolean(key.isDeveloper, def.isDeveloper)
        set(value) = edit { putBoolean(key.isDeveloper, value) }

    override fun clear() = edit { clear() }


    private fun edit(func: SharedPreferences.Editor.() -> Unit) {
        preferences.edit().apply(func).apply()
    }
}