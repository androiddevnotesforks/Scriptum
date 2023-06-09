package sgtmelon.scriptum.parent.ui.screen.dialogs.sheet

import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.dialogs.sheet.RepeatSheetDialog
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.parent.ui.parts.dialog.SheetDialogPart
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control [RepeatSheetDialog].
 */
class RepeatSheetDialogUi : SheetDialogPart(
    R.id.repeat_container,
    R.id.repeat_navigation,
    R.string.dialog_title_repeat,
    R.array.pref_repeat
) {

    fun repeat(value: Repeat) = apply { getButton(value).click() }

    companion object {
        inline operator fun invoke(func: RepeatSheetDialogUi.() -> Unit): RepeatSheetDialogUi {
            return RepeatSheetDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}