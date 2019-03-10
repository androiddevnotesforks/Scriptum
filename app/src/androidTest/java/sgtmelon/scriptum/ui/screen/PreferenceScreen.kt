package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.basic.BasicMatch

class PreferenceScreen {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    companion object {
        operator fun invoke(func: PreferenceScreen.() -> Unit) = PreferenceScreen().apply { func() }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent() {
            onDisplay(R.id.preference_parent_container)

            onDisplayToolbar(R.id.toolbar_container, R.string.title_preference)

            // TODO больше assert
        }

    }

}