package sgtmelon.scriptum.ui.screen.intro

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.office.annot.IntroAnn

class IntroAssert : BasicMatch() {

    fun onDisplayContent(position: Int) {
        onDisplay(R.id.info_title_text, IntroAnn.title[position])
        onDisplay(R.id.info_details_text, IntroAnn.details[position])
    }

    fun isEnableEndButton(position: Int) =
            isEnable(R.id.intro_end_button, position == IntroAnn.count - 1)

    fun onDisplayEndButton() = onDisplay(R.id.intro_end_button, R.string.info_intro_button)

}