package sgtmelon.scriptum.cleanup.presentation.control.system.callback

import sgtmelon.scriptum.cleanup.presentation.control.system.ClipboardControl

/**
 * Interface for communicate with [ClipboardControl].
 */
interface IClipboardControl {
    fun copy(text: String)
}