package sgtmelon.scriptum.presentation.control.toolbar.tint

import sgtmelon.scriptum.domain.model.annotation.Color

/**
 * Interface for [HolderTintControl].
 */
interface IHolderTintControl {
    fun setupColor(@Color color: Int)
}