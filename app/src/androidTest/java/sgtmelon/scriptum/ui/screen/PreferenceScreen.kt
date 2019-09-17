package sgtmelon.scriptum.ui.screen

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.screen.ui.preference.PreferenceActivity
import sgtmelon.scriptum.screen.ui.preference.PreferenceFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Class for UI control of [PreferenceActivity], [PreferenceFragment]
 */
class PreferenceScreen : ParentUi() {

    fun assert(func: Assert.() -> Unit = {}) = Assert().apply { func() }

    fun onPressBack() = pressBack()

    fun onClickClose() = action { onClickToolbarButton() }


    // TODO больше assert
    class Assert : BasicMatch() {
        init {
            onDisplay(R.id.preference_parent_container)

            onDisplayToolbar(R.id.toolbar_container, R.string.title_preference)
        }
    }

    companion object {
        operator fun invoke(func: PreferenceScreen.() -> Unit) = PreferenceScreen().apply {
            assert()
            func()
        }
    }

}