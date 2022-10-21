package sgtmelon.scriptum.infrastructure.model.item

import androidx.annotation.ColorRes

/**
 * Model which describes views colors. Used basically for different color indicators.
 */
data class ColorItem(
    @ColorRes val stroke: Int,
    @ColorRes val fill: Int,
    @ColorRes val content: Int
)