package sgtmelon.scriptum.domain.model.state

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import androidx.annotation.StringDef
import androidx.fragment.app.Fragment
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate

/**
 * State for dialogs which give us know open them or not.
 */
class OpenState {

    /**
     * Value for [block] realisation.
     */
    @RunPrivate var blockHandler = Handler()

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
    @Tag var tag: String = Tag.ND

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

    fun tryInvoke(@Tag tag: String, func: () -> Unit) {
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

    /**
     * Try call func if it possible.
     */
    fun tryCall(func: () -> Unit) {
        if (!changeEnabled) return

        if (!value) {
            func()
        }
    }

    /**
     * Use when need block [OpenState] for [time].
     */
    fun block(time: Long) {
        /**
         * Need deny any changes in [OpenState] what can happen in postDelayed time.
         */
        changeEnabled = false

        blockHandler.removeCallbacksAndMessages(null)
        blockHandler.postDelayed({ onBlockRunnable() }, time)
    }

    @RunPrivate fun onBlockRunnable() {
        changeEnabled = true
        clear()
    }

    fun clear() {
        if (skipClear) {
            skipClear = false
            return
        }

        if (!changeEnabled) return

        value = false
        tag = Tag.ND
    }

    /**
     * Call in [Activity.onDestroy]/[Fragment.onDestroy] where use [block].
     */
    fun clearBlockCallback() = blockHandler.removeCallbacksAndMessages(null)

    fun get(bundle: Bundle?) {
        bundle?.let {
            changeEnabled = it.getBoolean(KEY_CHANGE)
            value = it.getBoolean(KEY_VALUE)
            tag = it.getString(KEY_TAG) ?: Tag.ND
        }
    }

    fun save(bundle: Bundle) = bundle.let {
        it.putBoolean(KEY_CHANGE, changeEnabled)
        it.putBoolean(KEY_VALUE, value)
        it.putString(KEY_TAG, tag)
    }

    @StringDef(Tag.ND, Tag.ANIM, Tag.DIALOG, Tag.DIALOG)
    annotation class Tag {
        companion object {
            private const val TAG_PREFIX = "TAG"

            const val ND = "${TAG_PREFIX}_ND"
            const val ANIM = "${TAG_PREFIX}_ANIMATION"
            const val DIALOG = "${TAG_PREFIX}_DIALOG"
        }
    }

    companion object {
        private const val KEY_PREFIX = "OPEN_STATE"

        @RunPrivate const val KEY_CHANGE = "${KEY_PREFIX}_CHANGE"
        @RunPrivate const val KEY_VALUE = "${KEY_PREFIX}_VALUE"
        @RunPrivate const val KEY_TAG = "${KEY_PREFIX}_TAG"
    }

}