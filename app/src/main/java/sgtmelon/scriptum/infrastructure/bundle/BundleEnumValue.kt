package sgtmelon.scriptum.infrastructure.bundle

import android.os.Bundle

class BundleEnumValue<T : Enum<T>>(private val key: String) : BundleValue {

    private var _value: T? = null
    val value: T? get() = _value

    @Suppress("UNCHECKED_CAST")
    override fun get(bundle: Bundle?) {
        _value = bundle?.get(key) as T?
    }

    override fun save(outState: Bundle) {
        outState.putSerializable(key, value)
    }
}