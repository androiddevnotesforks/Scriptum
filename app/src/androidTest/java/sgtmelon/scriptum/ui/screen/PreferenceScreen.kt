package sgtmelon.scriptum.ui.screen

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.screen.ui.preference.PreferenceActivity
import sgtmelon.scriptum.screen.ui.preference.PreferenceFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.dialog.ColorDialogUi

/**
 * Class for UI control of [PreferenceActivity], [PreferenceFragment].
 */
class PreferenceScreen : ParentUi(), ColorDialogUi.Callback {

    //region Views

    private val parentContainer = getViewById(R.id.preference_parent_container)
    private val toolbar = getToolbar(R.string.title_preference)

    private val colorTitle = getViewByText(R.string.title_note_color)

    //endregion

    fun onPressBack() = pressBack()

    fun onClickClose() {
        getToolbarButton().click()
    }


    fun onClickDefaultColor(@Color check: Int, func: ColorDialogUi.() -> Unit) {
        colorTitle.click()
        ColorDialogUi.invoke(func, ColorDialogUi.Place.PREF, check, callback = this)
    }

    override fun onColorDialogResult(check: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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