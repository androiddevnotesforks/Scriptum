package sgtmelon.scriptum.infrastructure.screen.parent

import android.os.Bundle

interface ParentBundleProvider {

    fun getData(bundle: Bundle?)

    fun saveData(outState: Bundle)
}