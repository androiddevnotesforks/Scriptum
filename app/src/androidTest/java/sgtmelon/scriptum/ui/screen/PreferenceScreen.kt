package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.swipeUp
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.ColorDialogUi
import sgtmelon.scriptum.ui.dialog.RepeatDialogUi
import sgtmelon.scriptum.ui.part.toolbar.SimpleToolbar

/**
 * Class for UI control of [PreferenceActivity], [PreferenceFragment].
 */
class PreferenceScreen : ParentRecyclerScreen(R.id.recycler_view),
        ColorDialogUi.Callback,
        IPressBack {

    // TODO #TEST after migration on new library add assertion for items

    //region Views

    private val parentContainer = getViewById(R.id.preference_parent_container)
    private val toolbar = SimpleToolbar(R.string.title_preference)

    private val colorTitle = getViewByText(R.string.pref_title_note_color)
    private val repeatTitle = getViewByText(R.string.pref_title_alarm_repeat)

    //endregion

    fun onClickClose() {
        toolbar.getToolbarButton().click()
    }


    fun openColorDialog(@Color check: Int, func: ColorDialogUi.() -> Unit) {
        colorTitle.click()
        ColorDialogUi(func, ColorDialogUi.Place.PREF, check, callback = this)
    }

    override fun onColorDialogResult(check: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * TODO #TEST add assert summary
     */
    fun openRepeatDialog(func: RepeatDialogUi.() -> Unit) = apply {
        recyclerView.swipeUp()
        repeatTitle.click()
        RepeatDialogUi(func)
    }



    fun assert() = apply {
        parentContainer.isDisplayed()
        toolbar.assert()
    }

    companion object {
        operator fun invoke(func: PreferenceScreen.() -> Unit): PreferenceScreen {
            return PreferenceScreen().assert().apply(func)
        }
    }

}