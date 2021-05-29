package sgtmelon.scriptum.ui.dialog.parent

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import sgtmelon.safedialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*

/**
 * Parent class for UI control of [SingleDialog] with fixed size.
 */
abstract class ParentSelectDialogUi(
    @StringRes private val titleId: Int,
    @ArrayRes private val textArrayId: Int
) : ParentDialogUi(titleId, textArrayId) {

    abstract val initCheck: Int
    abstract var check: Int

    override fun onClickApply() = waitClose {
        if (check == initCheck) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
    }

    override fun assert() {
        super.assert()

        for ((i, name) in textArray.withIndex()) {
            getItem(name).isDisplayed().isChecked(isChecked = check == i)
        }

        applyButton.isDisplayed().isEnabled(isEnabled = check != initCheck) {
            withTextColor(R.attr.clAccent)
        }
    }
}