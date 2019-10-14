package sgtmelon.scriptum.model.state

import android.os.Bundle

/**
 * State for dialogs which give us know open them or not
 */
class OpenState {

    var value: Boolean = false

    inline fun tryInvoke(before: OpenState.() -> Unit = {}, func: () -> Unit) {
        apply(before)

        if (!value) {
            value = true
            func()
        }
    }

    fun clear() {
        value = false
    }

    fun get(bundle: Bundle?) {
        if(bundle == null) return
        value = bundle.getBoolean(KEY)
    }

    fun save(bundle: Bundle) = bundle.putBoolean(KEY, value)

    companion object {
        const val KEY = "INTENT_STATE_OPEN"
    }

}