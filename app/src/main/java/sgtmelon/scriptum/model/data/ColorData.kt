package sgtmelon.scriptum.model.data

import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.ColorItem

/**
 * Описание цветов заметок
 */
object ColorData {

    val light = intArrayOf(
            R.color.note_red_light, R.color.note_purple_light,
            R.color.note_indigo_light, R.color.note_blue_light,
            R.color.note_teal_light, R.color.note_green_light,
            R.color.note_yellow_light, R.color.note_orange_light,
            R.color.note_brown_light, R.color.note_blue_grey_light,
            R.color.note_white_light
    )

    val accent = intArrayOf(
            R.color.note_red_accent, R.color.note_purple_accent,
            R.color.note_indigo_accent, R.color.note_blue_accent,
            R.color.note_teal_accent, R.color.note_green_accent,
            R.color.note_yellow_accent, R.color.note_orange_accent,
            R.color.note_brown_accent, R.color.note_blue_grey_accent,
            R.color.note_white_accent
    )

    val dark = intArrayOf(
            R.color.note_red_dark, R.color.note_purple_dark,
            R.color.note_indigo_dark, R.color.note_blue_dark,
            R.color.note_teal_dark, R.color.note_green_dark,
            R.color.note_yellow_dark, R.color.note_orange_dark,
            R.color.note_brown_dark, R.color.note_blue_grey_dark,
            R.color.note_white_dark
    )

    val size = if (light.size == accent.size && accent.size == dark.size) light.size else {
        throw IndexOutOfBoundsException("Arrays have different size")
    }

    fun getColorItem(@Theme theme: Int, @Color color: Int) = if (theme == Theme.LIGHT) {
        ColorItem(dark[color], light[color], dark[color])
    } else {
        ColorItem(dark[color], dark[color], light[color])
    }

}