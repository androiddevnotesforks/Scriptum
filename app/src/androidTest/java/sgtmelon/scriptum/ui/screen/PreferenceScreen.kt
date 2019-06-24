package sgtmelon.scriptum.ui.screen

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.screen.view.preference.PreferenceActivity
import sgtmelon.scriptum.screen.view.preference.PreferenceFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Класс для ui контроля экрана [PreferenceActivity], [PreferenceFragment]
 *
 * @author SerjantArbuz
 */
class PreferenceScreen : ParentUi() {

    fun assert(func: Assert.() -> Unit = {}) = Assert().apply { func() }

    fun onPressBack() = pressBack()

    fun onClickClose() = action { onClickToolbarButton() }

    companion object {
        operator fun invoke(func: PreferenceScreen.() -> Unit) = PreferenceScreen().apply {
            assert()
            func()
        }
    }

    class Assert : BasicMatch() {

        // TODO больше assert
        init {
            onDisplay(R.id.preference_parent_container)

            onDisplayToolbar(R.id.toolbar_container, R.string.title_preference)
        }

    }

}