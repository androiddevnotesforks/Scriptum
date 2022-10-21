package sgtmelon.scriptum.cleanup.domain.model.data

import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.item.ColorItem
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed

/**
 * Data of note colors
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

    init {
        if (light.size != accent.size && accent.size != dark.size) {
            throw IndexOutOfBoundsException("Color arrays with different sizes")
        }
    }

    fun getColorItem(theme: ThemeDisplayed, color: Color): ColorItem {
        val ordinal = color.ordinal

        return if (theme == ThemeDisplayed.LIGHT) {
            ColorItem(dark[ordinal], light[ordinal], dark[ordinal])
        } else {
            ColorItem(dark[ordinal], dark[ordinal], light[ordinal])
        }
    }
}