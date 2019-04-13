package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.screen.view.pref.PrefActivity
import sgtmelon.scriptum.screen.view.pref.PrefFragment
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Класс для ui контроля экрана [PrefActivity], [PrefFragment]
 *
 * @author SerjantArbuz
 */
class PreferenceScreen {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    class Assert : BasicMatch() {

        fun onDisplayContent() {
            onDisplay(R.id.preference_parent_container)

            onDisplayToolbar(R.id.toolbar_container, R.string.title_preference)

            // TODO больше assert
        }

    }

}