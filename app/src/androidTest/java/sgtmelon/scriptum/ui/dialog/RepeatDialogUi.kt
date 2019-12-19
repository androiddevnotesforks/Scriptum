package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.dialog.SheetRepeatDialog
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi

/**
 * Class for UI control [SheetRepeatDialog].
 */
class RepeatDialogUi : ParentUi(), IDialogUi {

    // TODO #TEST add parent sheet

    private val navigationContainer = getViewById(R.id.repeat_container)
    private val navigationView = getViewById(R.id.repeat_navigation)

    private val titleText = getViewByText(R.string.dialog_title_repeat)
    private val repeat0Button = getViewByText(R.string.dialog_repeat_0)
    private val repeat1Button = getViewByText(R.string.dialog_repeat_1)
    private val repeat2Button = getViewByText(R.string.dialog_repeat_2)
    private val repeat3Button = getViewByText(R.string.dialog_repeat_3)
    private val repeat4Button = getViewByText(R.string.dialog_repeat_4)

    fun onClickRepeat(@Repeat repeat: Int) {
        when(repeat) {
            Repeat.MIN_10 -> repeat0Button.click()
            Repeat.MIN_30 -> repeat1Button.click()
            Repeat.MIN_60 -> repeat2Button.click()
            Repeat.MIN_180 -> repeat3Button.click()
            Repeat.MIN_1440 -> repeat4Button.click()
        }
    }

    fun onCloseSwipe() = waitClose { navigationView.swipeDown() }

    fun assert() {
        navigationContainer.isDisplayed().withBackground(when(theme == Theme.LIGHT) {
            true -> R.drawable.bg_dialog_light
            false -> R.drawable.bg_dialog_dark
        })

        navigationView.isDisplayed()

        titleText.isDisplayed().withTextColor(R.attr.clContentSecond)
        repeat0Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        repeat1Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        repeat2Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        repeat3Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        repeat4Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
    }

    companion object {
        operator fun invoke(func: RepeatDialogUi.() -> Unit): RepeatDialogUi {
            return RepeatDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }

}