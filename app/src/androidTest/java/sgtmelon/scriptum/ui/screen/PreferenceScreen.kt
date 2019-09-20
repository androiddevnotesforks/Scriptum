package sgtmelon.scriptum.ui.screen

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.screen.ui.preference.PreferenceActivity
import sgtmelon.scriptum.screen.ui.preference.PreferenceFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.basic.click
import sgtmelon.scriptum.basic.isDisplayed

/**
 * Class for UI control of [PreferenceActivity], [PreferenceFragment]
 */
class PreferenceScreen : ParentUi() {

    //region Views

    private val parentContainer = getViewById(R.id.preference_parent_container)
    private val toolbar = getToolbar(R.string.title_preference)

    //endregion

    fun onPressBack() = pressBack()

    fun onClickClose() {
        getToolbarButton().click()
    }


    fun assert() {
        parentContainer.isDisplayed()
        toolbar.isDisplayed()
    }

    companion object {
        operator fun invoke(func: PreferenceScreen.() -> Unit) =
                PreferenceScreen().apply { assert() }.apply(func)
    }

}