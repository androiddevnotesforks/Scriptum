package sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint

import sgtmelon.scriptum.cleanup.domain.model.annotation.Color

/**
 * Interface for [HolderTintControl].
 */
interface IHolderTintControl {
    fun setupColor(@Color color: Int)
}