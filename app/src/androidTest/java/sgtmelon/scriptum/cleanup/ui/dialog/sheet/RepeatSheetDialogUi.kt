package sgtmelon.scriptum.cleanup.ui.dialog.sheet

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.dialog.sheet.RepeatSheetDialog
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control [RepeatSheetDialog].
 */
class RepeatSheetDialogUi : ParentSheetDialogUi(R.id.repeat_container, R.id.repeat_navigation) {

    //region Views

    private val titleText = getViewByText(R.string.dialog_title_repeat)
    private val repeat0Button = getViewByText(R.string.pref_repeat_0)
    private val repeat1Button = getViewByText(R.string.pref_repeat_1)
    private val repeat2Button = getViewByText(R.string.pref_repeat_2)
    private val repeat3Button = getViewByText(R.string.pref_repeat_3)
    private val repeat4Button = getViewByText(R.string.pref_repeat_4)

    //endregion

    fun onClickRepeat(repeat: Repeat) {
        when (repeat) {
            Repeat.MIN_10 -> repeat0Button.click()
            Repeat.MIN_30 -> repeat1Button.click()
            Repeat.MIN_60 -> repeat2Button.click()
            Repeat.MIN_180 -> repeat3Button.click()
            Repeat.MIN_1440 -> repeat4Button.click()
        }
    }


    override fun assert() {
        super.assert()

        titleText.isDisplayed().withTextColor(R.attr.clContentSecond)
        repeat0Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        repeat1Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        repeat2Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        repeat3Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        repeat4Button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
    }

    companion object {
        inline operator fun invoke(func: RepeatSheetDialogUi.() -> Unit): RepeatSheetDialogUi {
            return RepeatSheetDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}