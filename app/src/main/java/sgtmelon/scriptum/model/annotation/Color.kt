package sgtmelon.scriptum.model.annotation

import androidx.annotation.IntDef

/**
 * Для описания стандартных цветов
 *
 * @author SerjantArbuz
 */
@IntDef(Color.red, Color.purple, Color.indigo,
        Color.blue, Color.teal, Color.green,
        Color.yellow, Color.orange, Color.brown,
        Color.blueGrey, Color.white
)
annotation class Color {

    companion object {
        const val red = 0
        const val purple = 1
        const val indigo = 2
        const val blue = 3
        const val teal = 4
        const val green = 5
        const val yellow = 6
        const val orange = 7
        const val brown = 8
        const val blueGrey = 9
        const val white = 10

        val list = arrayListOf(
                red, purple, indigo,
                blue, teal, green,
                yellow, orange, brown,
                blueGrey, white
        )
    }

}