package sgtmelon.scriptum.ui.dialog.parent

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*

/**
 * Parent class for UI control of [SingleDialog] with fixed size.
 */
abstract class ParentSelectDialogUi(
    @StringRes titleId: Int,
    @ArrayRes textArrayId: Int?,
    textArray: Array<String>?
) : ParentDialogUi(titleId, textArrayId, textArray) {

    constructor(
        @StringRes titleId: Int,
        @ArrayRes textArrayId: Int
    ) : this(titleId, textArrayId, textArray = null)

    constructor(
        @StringRes titleId: Int,
        textArray: Array<String>
    ) : this(titleId, textArrayId = null, textArray)

    abstract val initCheck: Int
    abstract var check: Int

    override fun onClickApply() = waitClose {
        if (check == initCheck) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
    }

    override fun assert() {
        super.assert()

        for (i in itemArray.indices) {
            getItem(i).isDisplayed().isChecked(isChecked = check == i)
        }

        applyButton.isDisplayed().isEnabled(isEnabled = check != initCheck) {
            withTextColor(R.attr.clAccent)
        }
    }
}