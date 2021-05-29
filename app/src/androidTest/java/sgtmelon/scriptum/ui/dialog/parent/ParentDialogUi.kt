package sgtmelon.scriptum.ui.dialog.parent

import androidx.annotation.ArrayRes
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.basic.extension.withTextColor
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentRecyclerScreen

/**
 * Parent class for UI control of all list dialogs.
 */
abstract class ParentDialogUi(
    @StringRes private val titleId: Int,
    @ArrayRes private val textArrayId: Int
) : ParentRecyclerScreen(R.id.select_dialog_listview),
    IDialogUi {

    //region Views

    /**
     * Exclude this parent for prevent match error with summary (similar strings).
     */
    private val preferenceList = getViewById(R.id.recycler_view)

    private val titleText = getViewByText(titleId).excludeParent(preferenceList)

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    protected val applyButton = getViewByText(R.string.dialog_button_apply)

    protected val textArray: Array<String> = context.resources.getStringArray(textArrayId)

    fun getItem(name: String) = getViewByText(name).excludeParent(preferenceList)

    //endregion

    fun onClickCancel() = waitClose { cancelButton.click() }

    abstract fun onClickApply()

    @CallSuper open fun assert() {
        recyclerView.isDisplayed()
        titleText.isDisplayed()

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
    }

}