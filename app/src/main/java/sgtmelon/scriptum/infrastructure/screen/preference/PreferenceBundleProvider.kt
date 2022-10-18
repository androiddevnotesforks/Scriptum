package sgtmelon.scriptum.infrastructure.screen.preference

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Preference.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Preference.Intent
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen

/**
 * Bundle data provider for [PreferenceActivity] screen.
 */
class PreferenceBundleProvider {

    private var _screen: PreferenceScreen? = null
    val screen: PreferenceScreen? get() = _screen

    fun getData(bundle: Bundle?) {
        val ordinal = bundle?.getInt(Intent.SCREEN, Default.SCREEN) ?: Default.SCREEN
        _screen = PreferenceScreen.values().getOrNull(ordinal)
    }

    fun saveData(outState: Bundle) {
        val ordinal = screen?.ordinal ?: return
        outState.putInt(Intent.SCREEN, ordinal)
    }
}