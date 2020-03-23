package sgtmelon.scriptum.model.item

import androidx.annotation.ColorRes
import sgtmelon.scriptum.presentation.adapter.ColorAdapter
import sgtmelon.scriptum.presentation.dialog.ColorDialog

/**
 * Model for describe colors of different views.
 *
 * Use in:
 * - for [ColorDialog], list element in [ColorAdapter];
 * - for notes and notification color indicators.
 */
class ColorItem(@ColorRes val stroke: Int, @ColorRes val fill: Int, @ColorRes val content: Int)