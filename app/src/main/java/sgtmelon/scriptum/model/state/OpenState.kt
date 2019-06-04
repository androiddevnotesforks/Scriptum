package sgtmelon.scriptum.model.state

import android.os.Bundle

/**
 * Состояние для диалогов, сохраняющее открыт ли он
 *
 * @author SerjantArbuz
 */
class OpenState {

    var value: Boolean = false

    inline fun tryInvoke(func: () -> Unit) {
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