package sgtmelon.scriptum.model.state

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment

/**
 * State for dialogs which give us know open them or not.
 */
class OpenState {

    /**
     * Value for [block] realisation.
     */
    private val handler = Handler()

    /**
     * Value for control open. If something was opened - TRUE, else - FALSE.
     */
    var value: Boolean = false

    /**
     * Variable for control changing of [value].
     */
    var changeEnabled: Boolean = true

    /**
     * Use for open dialog chain (for block actions during dialogs close/open)
     */
    var tag: String = TAG_ND

    /**
     * Use when need skip next [clear], e.g. on dialog dismiss
     */
    var skipClear = false

    fun tryInvoke(func: () -> Unit) {
        if (!changeEnabled) return

        if (!value) {
            value = true
            func()
        }
    }

    fun tryInvoke(tag: String, func: () -> Unit) {
        if (!changeEnabled) return

        if (!value || this.tag == tag) {
            value = true
            func()
        }
    }

    fun <T> tryReturnInvoke(func: () -> T): T? {
        if (!changeEnabled) return null

        if (!value) {
            value = true
            return func()
        }

        return null
    }

    fun tryCall(func: () -> Unit) {
        if (!value) {
            func()
        }
    }

    /**
     * Use when need block [OpenState] for [time]
     */
    fun block(time: Long?) {
        if (time == null) return

        /**
         * Need deny any changes in [OpenState] what can happen in postDelayed time.
         */
        changeEnabled = false

        handler.postDelayed({
            changeEnabled = true
            clear()
        }, time)
    }

    fun clear() {
        if (skipClear) {
            skipClear = false
            return
        }

        if (!changeEnabled) return

        value = false
        tag = TAG_ND
    }

    /**
     * Call in [Activity.onDestroy]/[Fragment.onDestroy] where use [block].
     */
    fun clearBlockCallback() = handler.removeCallbacksAndMessages(null)

    fun get(bundle: Bundle?) {
        bundle?.let {
            changeEnabled = it.getBoolean(KEY_CHANGE)
            value = it.getBoolean(KEY_VALUE)
            tag = it.getString(KEY_TAG) ?: TAG_ND
        }
    }

    fun save(bundle: Bundle) = bundle.let {
        it.putBoolean(KEY_CHANGE, changeEnabled)
        it.putBoolean(KEY_VALUE, value)
        it.putString(KEY_TAG, tag)
    }

    companion object {
        private const val KEY_PREFIX = "OPEN_STATE"

        private const val KEY_CHANGE = "${KEY_PREFIX}_CHANGE"
        private const val KEY_VALUE = "${KEY_PREFIX}_VALUE"
        private const val KEY_TAG = "${KEY_PREFIX}_TAG"


        private const val TAG_PREFIX = "TAG"

        const val TAG_ND = "${TAG_PREFIX}_ND"
        const val TAG_ANIMATION = "${TAG_PREFIX}_ANIMATION"
        const val TAG_OPTIONS = "${TAG_PREFIX}_OPTIONS"
        const val TAG_DATE_TIME = "${TAG_PREFIX}_DATE_TIME"
    }

}