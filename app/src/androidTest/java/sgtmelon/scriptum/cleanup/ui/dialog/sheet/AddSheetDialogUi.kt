package sgtmelon.scriptum.cleanup.ui.dialog.sheet

import sgtmelon.common.utils.getCalendarText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.click
import sgtmelon.scriptum.cleanup.basic.extension.isDisplayed
import sgtmelon.scriptum.cleanup.basic.extension.isEnabled
import sgtmelon.scriptum.cleanup.basic.extension.withTextColor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.dialog.sheet.AddSheetDialog
import sgtmelon.scriptum.cleanup.testData.DbDelegator
import sgtmelon.scriptum.cleanup.testData.State
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen

/**
 * Class for UI control [AddSheetDialog].
 */
class AddSheetDialogUi : ParentSheetDialogUi(R.id.add_container, R.id.add_navigation) {

    //region Views

    private val titleText = getViewByText(R.string.dialog_title_add_note)
    private val textButton = getViewByText(R.string.dialog_add_text)
    private val rollButton = getViewByText(R.string.dialog_add_roll)

    //endregion

    fun createText(
        item: NoteItem.Text,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) {
        /**
         * Was assertion error in tests where time difference was 1 minute. I think it was
         * happened when calendar time was ~00:59 on note create inside [DbDelegator]. But time
         * of actual note creation was ~01:.. (after [DbDelegator] note was created).
         */
        item.create = getCalendarText()

        textButton.click()
        TextNoteScreen(func, State.NEW, item, isRankEmpty)
    }

    fun createRoll(
        item: NoteItem.Roll,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) {
        /**
         * Was assertion error in tests where time difference was 1 minute. I think it was
         * happened when calendar time was ~00:59 on note create inside [DbDelegator]. But time
         * of actual note creation was ~01:.. (after [DbDelegator] note was created).
         */
        item.create = getCalendarText()

        rollButton.click()
        RollNoteScreen(func, State.NEW, item, isRankEmpty)
    }


    override fun assert() {
        super.assert()

        titleText.isDisplayed().withTextColor(R.attr.clContentSecond)
        textButton.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        rollButton.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
    }

    companion object {
        operator fun invoke(func: AddSheetDialogUi.() -> Unit): AddSheetDialogUi {
            return AddSheetDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}