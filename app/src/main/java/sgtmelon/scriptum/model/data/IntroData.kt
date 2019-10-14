package sgtmelon.scriptum.model.data

import sgtmelon.scriptum.R
import sgtmelon.scriptum.screen.ui.intro.IntroActivity

/**
 * Data for [IntroActivity]
 */
object IntroData {

    val icon = intArrayOf(
            R.drawable.ic_note_add,
            R.drawable.ic_palette,
            R.drawable.ic_rank,
            R.drawable.ic_visible_enter,
            R.drawable.ic_notifications,
            R.drawable.ic_bind_roll
    )

    val title = intArrayOf(
            R.string.info_intro_title_1,
            R.string.info_intro_title_2,
            R.string.info_intro_title_3,
            R.string.info_intro_title_4,
            R.string.info_intro_title_5,
            R.string.info_intro_title_6
    )

    val details = intArrayOf(
            R.string.info_intro_details_1,
            R.string.info_intro_details_2,
            R.string.info_intro_details_3,
            R.string.info_intro_details_4,
            R.string.info_intro_details_5,
            R.string.info_intro_details_6
    )

    val count = icon.size

}