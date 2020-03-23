package sgtmelon.scriptum.presentation.control.system.callback

import sgtmelon.scriptum.presentation.control.system.ClipboardControl

/**
 * Interface for communicate with [ClipboardControl].
 */
interface IClipboardControl {
    fun copy(text: String)
}