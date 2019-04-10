package sgtmelon.scriptum.model.data

import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.item.ColorItem
import sgtmelon.scriptum.office.annot.def.ThemeDef

/**
 * Описание цветов заметок
 *
 * @author SerjantArbuz
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

    val dark = intArrayOf(
            R.color.note_red_dark, R.color.note_purple_dark,
            R.color.note_indigo_dark, R.color.note_blue_dark,
            R.color.note_teal_dark, R.color.note_green_dark,
            R.color.note_yellow_dark, R.color.note_orange_dark,
            R.color.note_brown_dark, R.color.note_blue_grey_dark,
            R.color.note_white_dark
    )

    val size = if (light.size == dark.size) light.size else {
        throw IndexOutOfBoundsException("Arrays have different size")
    }

    fun getColorList(theme: Int) = ArrayList<ColorItem>().apply {
        for (i in 0 until size) add(if (theme == ThemeDef.light) {
            ColorItem(light[i], dark[i], dark[i])
        } else {
            ColorItem(dark[i], dark[i], light[i])
        })
    }

}
