package sgtmelon.scriptum.ui.screen.intro

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.office.data.IntroData

class IntroAssert : BasicMatch() {

    fun onDisplayContent(position: Int) {
        onDisplay(R.id.info_title_text, IntroData.title[position])
        onDisplay(R.id.info_details_text, IntroData.details[position])
    }

    fun isEnableEndButton(position: Int) =
            isEnable(R.id.intro_end_button, position == IntroData.count - 1)

    fun onDisplayEndButton() = onDisplay(R.id.intro_end_button, R.string.info_intro_button)

}