package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.swipeUp
import sgtmelon.scriptum.basic.extension.withBackgroundAttr
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.screen.ui.preference.PreferenceActivity
import sgtmelon.scriptum.screen.ui.preference.PreferenceFragment
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.dialog.ColorDialogUi
import sgtmelon.scriptum.ui.dialog.RepeatDialogUi

/**
 * Class for UI control of [PreferenceActivity], [PreferenceFragment].
 */
class PreferenceScreen : ParentUi(), ColorDialogUi.Callback, IPressBack {

    // TODO #TEST after migration on new library add assertion for items

    //region Views

    private val parentContainer = getViewById(R.id.preference_parent_container)
    private val toolbar = getToolbar(R.string.title_preference)

    private val list = getViewById(android.R.id.list)
    private val colorTitle = getViewByText(R.string.pref_title_note_color)
    private val repeatTitle = getViewByText(R.string.pref_title_alarm_repeat)

    //endregion

    fun onClickClose() {
        getToolbarButton().click()
    }


    fun openColorDialog(@Color check: Int, func: ColorDialogUi.() -> Unit) {
        colorTitle.click()
        ColorDialogUi.invoke(func, ColorDialogUi.Place.PREF, check, callback = this)
    }

    override fun onColorDialogResult(check: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * TODO #TEST add assert summary
     */
    fun openRepeatDialog(func: RepeatDialogUi.() -> Unit) = apply {
        list.swipeUp()
        repeatTitle.click()
        RepeatDialogUi.invoke(func)
    }



    fun assert() = apply {
        parentContainer.isDisplayed()
        toolbar.isDisplayed().withBackgroundAttr(R.attr.colorPrimary)
    }

    companion object {
        operator fun invoke(func: PreferenceScreen.() -> Unit): PreferenceScreen {
            return PreferenceScreen().assert().apply(func)
        }
    }

}