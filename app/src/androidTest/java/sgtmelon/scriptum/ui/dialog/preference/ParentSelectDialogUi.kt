package sgtmelon.scriptum.ui.dialog.preference

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import sgtmelon.safedialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentRecyclerScreen

/**
 * Parent class for UI control of [SingleDialog] with fixed size.
 */
abstract class ParentSelectDialogUi(
    @StringRes private val titleId: Int,
    @ArrayRes private val textArrayId: Int
) : ParentRecyclerScreen(R.id.select_dialog_listview),
    IDialogUi {

    abstract val initCheck: Int
    abstract var check: Int

    //region Views

    /**
     * Exclude this parent for prevent match error with summary (similar strings).
     */
    private val preferenceList = getViewById(R.id.recycler_view)

    private val titleText = getViewByText(titleId).excludeParent(preferenceList)

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    protected val textArray: Array<String> = context.resources.getStringArray(textArrayId)

    fun getItem(name: String) = getViewByText(name).excludeParent(preferenceList)

    //endregion

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply() = waitClose {
        if (check == initCheck) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
    }

    fun assert() {
        recyclerView.isDisplayed()
        titleText.isDisplayed()

        for ((i, name) in textArray.withIndex()) {
            getItem(name).isDisplayed().isChecked(isChecked = check == i)
        }

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        applyButton.isDisplayed().isEnabled(isEnabled = check != initCheck) {
            withTextColor(R.attr.clAccent)
        }
    }
}