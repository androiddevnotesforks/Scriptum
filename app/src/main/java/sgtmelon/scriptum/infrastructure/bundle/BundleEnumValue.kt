package sgtmelon.scriptum.infrastructure.bundle

import android.os.Bundle

class BundleEnumValue<T : Enum<T>>(private val key: String) {

    private var _value: T? = null
    val value: T? get() = _value

    @Suppress("UNCHECKED_CAST")
    fun getData(bundle: Bundle?) {
        _value = bundle?.get(key) as T?
    }

    fun saveData(outState: Bundle) {
        outState.putSerializable(key, value)
    }
}