package sgtmelon.scriptum.infrastructure.preferences

import android.content.SharedPreferences
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider

/**
 * Application settings (keys) must be stored via this class.
 */
class PreferencesImpl(
    private val key: PreferencesKeyProvider,
    private val def: PreferencesDefProvider,
    private val preferences: SharedPreferences
) : Preferences {

    /** Removed from use (Intro screen) since: 04.11.22 */
    override var isFirstStart: Boolean
        get() = preferences.getBoolean(key.isFirstStart, def.isFirstStart)
        set(value) = edit { putBoolean(key.isFirstStart, value) }

    // App settings

    override var theme: Int
        get() = preferences.getInt(key.theme, def.theme)
        set(value) = edit { putInt(key.theme, value) }

    // Backup settings

    /** Change of this variable happen inside preference.xml screen (or inside UI tests). */
    override var isBackupSkipImports: Boolean
        get() = preferences.getBoolean(key.isBackupSkipImports, def.isBackupSkipImports)
        set(value) = edit { putBoolean(key.isBackupSkipImports, value) }

    // Note settings

    override var sort: Int
        get() = preferences.getInt(key.sort, def.sort)
        set(value) = edit { putInt(key.sort, value) }

    override var defaultColor: Int
        get() = preferences.getInt(key.defaultColor, def.defaultColor)
        set(value) = edit { putInt(key.defaultColor, value) }

    /** Change of this variable happen inside preference.xml screen (or inside UI tests). */
    override var isPauseSaveOn: Boolean
        get() = preferences.getBoolean(key.isPauseSaveOn, def.isPauseSaveOn)
        set(value) = edit { putBoolean(key.isPauseSaveOn, value) }

    /** Change of this variable happen inside preference.xml screen (or inside UI tests). */
    override var isAutoSaveOn: Boolean
        get() = preferences.getBoolean(key.isAutoSaveOn, def.isAutoSaveOn)
        set(value) = edit { putBoolean(key.isAutoSaveOn, value) }

    override var savePeriod: Int
        get() = preferences.getInt(key.savePeriod, def.savePeriod)
        set(value) = edit { putInt(key.savePeriod, value) }

    // Alarm settings

    override var repeat: Int
        get() = preferences.getInt(key.repeat, def.repeat)
        set(value) = edit { putInt(key.repeat, value) }

    override var signal: String
        get() = preferences.getString(key.signal, def.signal) ?: def.signal
        set(value) = edit { putString(key.signal, value) }

    override var melodyUri: String
        get() = preferences.getString(key.melodyUri, def.melodyUri) ?: def.melodyUri
        set(value) = edit { putString(key.melodyUri, value) }

    override var volumePercent: Int
        get() = preferences.getInt(key.volumePercent, def.volumePercent)
        set(value) = edit { putInt(key.volumePercent, value) }

    /** Change of this variable happen inside preference.xml screen (or inside UI tests). */
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