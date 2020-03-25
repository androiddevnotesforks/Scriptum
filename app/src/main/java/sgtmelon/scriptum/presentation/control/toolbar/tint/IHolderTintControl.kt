package sgtmelon.scriptum.presentation.control.toolbar.tint

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme

/**
 * Interface for [HolderTintControl].
 */
interface IHolderTintControl {
    fun setupColor(@Theme theme: Int, @Color color: Int)
}