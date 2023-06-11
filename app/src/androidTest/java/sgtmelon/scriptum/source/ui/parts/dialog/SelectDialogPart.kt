package sgtmelon.scriptum.source.ui.parts.dialog

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.source.ui.model.exception.NotEnabledException
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isChecked
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Parent class for UI control of [SingleDialog] with fixed size.
 */
abstract class SelectDialogPart<T>(
    @StringRes titleId: Int,
    @ArrayRes textArrayId: Int?,
    textArray: Array<String>?
) : ListDialogPart(titleId, textArrayId, textArray) {

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

    abstract fun click(value: T)

    override fun apply() = waitClose {
        if (check == initCheck) throw NotEnabledException(why = "select the same")

        applyButton.click()
    }

    override fun assert() {
        super.assert()

        for (i in itemArray.indices) {
            getItem(i).isDisplayed().isChecked(value = check == i)
        }

        applyButton.isDisplayed().isEnabled(value = check != initCheck) {
            withTextColor(R.attr.clAccent)
        }
    }
}