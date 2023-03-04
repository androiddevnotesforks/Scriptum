package sgtmelon.scriptum.infrastructure.bundle

import android.os.Bundle

@Deprecated("Use BundleValueImpl")
interface ParentBundleProvider {

    fun getData(bundle: Bundle?)

    fun saveData(outState: Bundle)
}