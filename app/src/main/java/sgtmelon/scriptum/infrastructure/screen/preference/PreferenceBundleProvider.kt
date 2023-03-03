package sgtmelon.scriptum.infrastructure.screen.preference

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.bundle.ParentBundleProvider
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Preference.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Preference.Intent
import sgtmelon.scriptum.infrastructure.model.exception.BundleException
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen
import sgtmelon.scriptum.infrastructure.utils.extensions.record

/**
 * Bundle data provider for [PreferenceActivity] screen.
 */
class PreferenceBundleProvider : ParentBundleProvider {

    private var _screen: PreferenceScreen? = null
    val screen: PreferenceScreen? get() = _screen

    override fun getData(bundle: Bundle?) {
        val ordinal = bundle?.getInt(Intent.SCREEN, Default.SCREEN) ?: Default.SCREEN
        _screen = PreferenceScreen.values().getOrNull(ordinal)

        if (screen == null) {
            BundleException(::screen).record()
        }
    }

    override fun saveData(outState: Bundle) {
        val ordinal = screen?.ordinal ?: return
        outState.putInt(Intent.SCREEN, ordinal)
    }
}