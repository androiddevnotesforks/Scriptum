package sgtmelon.scriptum.model.item

import androidx.annotation.ColorRes
import sgtmelon.scriptum.adapter.ColorAdapter
import sgtmelon.scriptum.dialog.ColorDialog

/**
 * Модель для описания цветов для различныъ view
 *
 * Используется:
 * - для [ColorDialog], элемент списка в [ColorAdapter]
 * - для индикатора у заметки и уведомления
 *
 * @author SerjantArbuz
 */
class ColorItem(@ColorRes val stroke: Int, @ColorRes val fill: Int, @ColorRes val content: Int)