package sgtmelon.scriptum.source.ui.parts.dialog

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import sgtmelon.safedialog.dialog.MultipleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.source.ui.model.exception.NotEnabledException
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isChecked
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control of [MultipleDialog] with fixed size.
 */
abstract class MultipleDialogPart(
    @StringRes private val titleId: Int,
    @ArrayRes private val textArrayId: Int,
    private val needOneSelect: Boolean
) : ListDialogPart(titleId, textArrayId) {

    abstract val initCheck: BooleanArray
    abstract val check: BooleanArray

    override fun apply() = waitClose {
        when {
            initCheck.contentEquals(check) -> {
                throw NotEnabledException(why = "select the same")
            }
            needOneSelect && !check.contains(true) -> {
                throw NotEnabledException(why = "nothing was selected")
            }
        }

        applyButton.click()
    }

    override fun assert() {
        super.assert()

        for (i in itemArray.indices) {
            getItem(i).isDisplayed().isChecked(check[i])
        }

        val isEnabled = if (needOneSelect) {
            !initCheck.contentEquals(check) && check.contains(true)
        } else {
            !initCheck.contentEquals(check)
        }

        applyButton.isDisplayed().isEnabled(isEnabled) {
            withTextColor(R.attr.clAccent)
        }
    }
}