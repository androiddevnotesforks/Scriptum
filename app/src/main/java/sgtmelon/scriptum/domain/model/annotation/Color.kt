package sgtmelon.scriptum.domain.model.annotation

import androidx.annotation.IntDef

/**
 * Describes standard colors
 */
@IntDef(
    Color.UNDEFINED,
    Color.RED, Color.PURPLE, Color.INDIGO,
    Color.BLUE, Color.TEAL, Color.GREEN,
    Color.YELLOW, Color.ORANGE, Color.BROWN,
    Color.BLUE_GREY, Color.WHITE
)
annotation class Color {
    companion object {
        const val UNDEFINED = -1
        const val RED = 0
        const val PURPLE = 1
        const val INDIGO = 2
        const val BLUE = 3
        const val TEAL = 4
        const val GREEN = 5
        const val YELLOW = 6
        const val ORANGE = 7
        const val BROWN = 8
        const val BLUE_GREY = 9
        const val WHITE = 10

        val list = arrayListOf(
            RED, PURPLE, INDIGO,
            BLUE, TEAL, GREEN,
            YELLOW, ORANGE, BROWN,
            BLUE_GREY, WHITE
        )
    }
}