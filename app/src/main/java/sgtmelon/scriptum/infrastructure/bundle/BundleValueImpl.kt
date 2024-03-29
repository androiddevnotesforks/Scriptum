package sgtmelon.scriptum.infrastructure.bundle

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.model.exception.BundleNullException
import java.io.Serializable

/**
 * Realization of [BundleValue] for [Serializable] classes.
 */
class BundleValueImpl<T : Serializable>(
    private val key: String,
    private val defaultValue: T? = null
) : BundleValue {

    private var _value: T? = null
    val value: T get() = _value ?: defaultValue ?: throw BundleNullException()

    @Suppress("UNCHECKED_CAST")
    override fun get(bundle: Bundle?) {
        _value = bundle?.get(key) as? T
    }

    override fun save(outState: Bundle) {
        outState.putSerializable(key, value)
    }
}