package sgtmelon.scriptum.infrastructure.bundle

import android.os.Bundle

/**
 * Abstraction for [get]/[save] values from/to [Bundle].
 */
interface BundleValue {

    fun get(bundle: Bundle?)

    fun save(outState: Bundle)

}