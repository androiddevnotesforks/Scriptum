package sgtmelon.scriptum.ui.screen.intro

import androidx.annotation.StringRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicMatch
import sgtmelon.scriptum.office.annot.IntroAnn

class IntroAssert : BasicMatch() {

    fun onDisplayTitle(@StringRes title: Int) = onDisplay(R.id.info_title_text, title)

    fun onDisplayDetails(@StringRes details: Int) = onDisplay(R.id.info_details_text, details)

    fun isEnableEndButton(screen: Int) =
            isEnable(R.id.end_button, screen >= IntroAnn.count - 2)

    fun onDisplayEndButton() = onDisplay(R.id.end_button, R.string.info_intro_button)

}