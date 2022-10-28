package sgtmelon.scriptum.develop.screen.print

import android.os.Bundle
import sgtmelon.scriptum.develop.model.PrintType
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Print.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Print.Intent

/**
 * Bundle data provider for [PrintDevelopActivity] screen.
 */
class PrintDevelopBundleProvider {

    private var _type: PrintType? = null
    val type: PrintType? get() = _type

    fun getData(bundle: Bundle?) {
        val ordinal = bundle?.getInt(Intent.TYPE, Default.TYPE) ?: Default.TYPE
        _type = PrintType.values().getOrNull(ordinal)
    }

    fun saveData(outState: Bundle) {
        val ordinal = type?.ordinal ?: return
        outState.putInt(Intent.TYPE, ordinal)
    }
}