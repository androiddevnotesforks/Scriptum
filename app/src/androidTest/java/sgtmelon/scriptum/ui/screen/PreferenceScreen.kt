package sgtmelon.scriptum.ui.screen

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.screen.view.pref.PrefActivity
import sgtmelon.scriptum.screen.view.pref.PrefFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Класс для ui контроля экрана [PrefActivity], [PrefFragment]
 *
 * @author SerjantArbuz
 */
class PreferenceScreen : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun onPressBack() = pressBack()

    fun onClickClose() = action { onClickToolbarButton() }

    class Assert : BasicMatch() {

        fun onDisplayContent() {
            onDisplay(R.id.preference_parent_container)

            onDisplayToolbar(R.id.toolbar_container, R.string.title_preference)

            // TODO больше assert
        }

    }

}