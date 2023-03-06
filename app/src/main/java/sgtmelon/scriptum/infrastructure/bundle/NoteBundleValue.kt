package sgtmelon.scriptum.infrastructure.bundle

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.model.init.NoteInit

class NoteBundleValue(private val key: String) : BundleValue {

    private var _value: NoteInit? = null
    val value: NoteInit get() = _value ?: throw NullPointerException("Got empty bundle data")

    override fun get(bundle: Bundle?) {
        _value = bundle?.getString(key)?.decode()
    }

    override fun save(outState: Bundle) {
        outState.putString(key, value.encode())
    }
}