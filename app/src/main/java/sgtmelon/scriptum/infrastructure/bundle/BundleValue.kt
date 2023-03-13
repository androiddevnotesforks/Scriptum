package sgtmelon.scriptum.infrastructure.bundle

import android.os.Bundle

interface BundleValue {

    fun get(bundle: Bundle?)

    fun save(outState: Bundle)

}