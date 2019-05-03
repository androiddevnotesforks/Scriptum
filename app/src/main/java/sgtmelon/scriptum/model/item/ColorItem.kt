package sgtmelon.scriptum.model.item

import androidx.annotation.ColorRes
import sgtmelon.scriptum.dialog.ColorDialog

/**
 * Модель для списка цветов в [ColorDialog], элемент списка в [ColorAdapter]
 *
 * @author SerjantArbuz
 */
class ColorItem(@ColorRes val fill: Int, @ColorRes val stroke: Int, @ColorRes val check: Int)