package sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint

import sgtmelon.scriptum.infrastructure.model.key.Color

/**
 * Interface for [ToolbarTintControl].
 */
interface IToolbarTintControl {

    fun setColorFrom(color: Color): IToolbarTintControl

    /**
     * Set end [color] and start animation if need.
     */
    fun startTint(color: Color)
}