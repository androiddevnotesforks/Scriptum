package sgtmelon.scriptum.infrastructure.preferences

import android.content.SharedPreferences
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.*
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider

/**
 * Application settings (keys) must be stored via this class.
 */
class PreferencesImpl(
    private val key: PreferencesKeyProvider,
    private val def: PreferencesDefProvider,
    private val preferences: SharedPreferences
) : Preferences {

    override var isFirstStart: Boolean
        get() = preferences.getBoolean(key.isFirstStart, def.isFirstStart)
        set(value) = edit { putBoolean(key.isFirstStart, value) }

    // App settings

    @Theme override var theme: Int
        get() = preferences.getInt(key.theme, def.theme)
        set(value) = edit { putInt(key.theme, value) }

    // Backup settings

    override var isBackupSkipImports: Boolean
        get() = preferences.getBoolean(key.isBackupSkipImports, def.isBackupSkipImports)
        set(value) = edit { putBoolean(key.isBackupSkipImports, value) }

    // Note settings

    @Sort override var sort: Int
        get() = preferences.getInt(key.sort, def.sort)
        set(value) = edit { putInt(key.sort, value) }

    @Color override var defaultColor: Int
        get() = preferences.getInt(key.defaultColor, def.defaultColor)
        set(value) = edit { putInt(key.defaultColor, value) }

    override var isPauseSaveOn: Boolean
        get() = preferences.getBoolean(key.isPauseSaveOn, def.isPauseSaveOn)
        set(value) = edit { putBoolean(key.isPauseSaveOn, value) }

    override var isAutoSaveOn: Boolean
        get() = preferences.getBoolean(key.isAutoSaveOn, def.isAutoSaveOn)
        set(value) = edit { putBoolean(key.isAutoSaveOn, value) }

    @SavePeriod override var savePeriod: Int
        get() = preferences.getInt(key.savePeriod, def.savePeriod)
        set(value) = edit { putInt(key.savePeriod, value) }

    // Alarm settings

    @Repeat override var repeat: Int
        get() = preferences.getInt(key.repeat, def.repeat)
        set(value) = edit { putInt(key.repeat, value) }

    override var signal: String
        get() = preferences.getString(key.signal, def.signal) ?: def.signal
        set(value) = edit { putString(key.signal, value) }

    /**
     * Access only from [SignalInteractor.getMelodyUri]/[SignalInteractor.setMelodyUri].
     */
    override var melodyUri: String
        get() = preferences.getString(key.melodyUri, def.melodyUri) ?: def.melodyUri
        set(value) = edit { putString(key.melodyUri, value) }

    override var volume: Int
        get() = preferences.getInt(key.volume, def.volume)
        set(value) = edit { putInt(key.volume, value) }

    override var isVolumeIncrease: Boolean
        get() = preferences.getBoolean(key.isVolumeIncrease, def.isVolumeIncrease)
        set(value) = edit { putBoolean(key.isVolumeIncrease, value) }

    // Developer settings

    override var isDeveloper: Boolean
        get() = preferences.getBoolean(key.isDeveloper, def.isDeveloper)
        set(value) = edit { putBoolean(key.isDeveloper, value) }

    override fun clear() = edit { clear() }


    private inline fun edit(func: SharedPreferences.Editor.() -> Unit) {
        preferences.edit().apply(func).apply()
    }
}