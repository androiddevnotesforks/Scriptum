package sgtmelon.scriptum.model.state

import android.os.Bundle

/**
 * State for dialogs which give us know open them or not
 */
class OpenState {

    var changeEnabled: Boolean = true
    var value: Boolean = false

    fun tryInvoke(before: OpenState.() -> Unit = {}, func: () -> Unit) {
        if (!changeEnabled) return

        apply(before)

        if (!value) {
            value = true
            func()
        }
    }

    fun clear() {
        if (!changeEnabled) return

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