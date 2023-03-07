package sgtmelon.scriptum.infrastructure.bundle

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.model.exception.BundleNullException

abstract class BundleJsonValue<T: Any>(
    private val key: String,
    private val defaultValue: T? = null
) : BundleValue {

    private var _value: T? = null
    val value: T get() = _value ?: defaultValue ?: throw BundleNullException()

    abstract fun decode(string: String?): T?

    abstract fun encode(): String

    override fun get(bundle: Bundle?) {
        _value = decode(bundle?.getString(key))
    }

    override fun save(outState: Bundle) {
        outState.putString(key, encode())
    }
}