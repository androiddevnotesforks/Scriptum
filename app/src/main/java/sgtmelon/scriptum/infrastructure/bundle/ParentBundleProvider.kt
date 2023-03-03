package sgtmelon.scriptum.infrastructure.bundle

import android.os.Bundle

interface ParentBundleProvider {

    fun getData(bundle: Bundle?)

    fun saveData(outState: Bundle)
}