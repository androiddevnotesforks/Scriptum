package sgtmelon.scriptum.infrastructure.model.state

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Open
import sgtmelon.scriptum.infrastructure.utils.DelayedJob

/**
 * Class which help prevent double click, different actions in single time.
 */
class OpenState(lifecycle: Lifecycle) {

    // TODO check how it works with rotation (open dialog - rotate - check ability to click)

    private val blockTimer = DelayedJob(lifecycle)

    /** Value for control block state. If something was opened - TRUE, else - FALSE. */
    var isBlocked: Boolean = false

    /** Variable for control ability to change [isBlocked] value. */
    var isChangeEnabled: Boolean = true

    /** Use for chains (e.g. dialogs, for block actions between dialogs close/open). */
    var tag: String = Tag.ND

    /**
     * Use when need skip next [clear]. For example, on dialog dismiss when we are using [tag].
     * In positive situation (when need open dialogs chain) - [skipClear] helps us,
     * otherwise - [clear] happened.
     */
    var skipClear = false

    /**
     * [withSwitch] == false - means call [func] without future block.
     */
    inline fun attempt(withSwitch: Boolean = true, func: () -> Unit) {
        if (!isChangeEnabled || isBlocked) return

        if (withSwitch) {
            isBlocked = true
        }

        func()
    }

    inline fun attempt(tag: String, func: () -> Unit) {
        if (!isChangeEnabled) return

        if (!isBlocked || this.tag == tag) {
            isBlocked = true
            func()
        }
    }

    inline fun ifNotBlocked(func: () -> Unit) {
        if (isBlocked) return
        func()
    }

    /**
     * Use when need block calls for [time] period.
     */
    fun block(time: Long) {
        /** Need deny any changes which can happen in await time. */
        isChangeEnabled = false

        blockTimer.start(time) {
            isChangeEnabled = true
            clear()
        }
    }

    fun clear() {
        if (skipClear) {
            skipClear = false
            return
        }

        if (!isChangeEnabled) return

        isBlocked = false
        tag = Tag.ND
    }

    fun save(bundle: Bundle) {
        bundle.putBoolean(Open.Intent.KEY_CHANGE, isChangeEnabled)
        bundle.putBoolean(Open.Intent.KEY_VALUE, isBlocked)
        bundle.putString(Open.Intent.KEY_TAG, tag)
    }

    fun restore(bundle: Bundle?) {
        if (bundle == null) return

        isChangeEnabled = bundle.getBoolean(Open.Intent.KEY_CHANGE)
        isBlocked = bundle.getBoolean(Open.Intent.KEY_VALUE)
        tag = bundle.getString(Open.Intent.KEY_TAG, Tag.ND)
    }

    object Tag {
        private const val TAG_PREFIX = "TAG"
        const val ND = "${TAG_PREFIX}_ND"
        const val ANIM = "${TAG_PREFIX}_ANIMATION"
        const val DIALOG = "${TAG_PREFIX}_DIALOG"
    }
}