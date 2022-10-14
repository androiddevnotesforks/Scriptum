package sgtmelon.scriptum.cleanup.domain.model.item

import androidx.annotation.ColorRes
import sgtmelon.scriptum.cleanup.presentation.dialog.ColorDialog
import sgtmelon.scriptum.infrastructure.adapter.ColorAdapter

/**
 * Model for describe colors of different views.
 *
 * Use in:
 * - for [ColorDialog], list element in [ColorAdapter];
 * - for notes and notification color indicators.
 */
data class ColorItem(@ColorRes val stroke: Int, @ColorRes val fill: Int, @ColorRes val content: Int)