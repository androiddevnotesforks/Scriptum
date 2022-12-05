package sgtmelon.scriptum.parent.ui.parts.dialog

import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import org.hamcrest.Matcher
import sgtmelon.safedialog.dialog.parent.BlankMenuSheetDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.parent.ui.feature.DialogUi
import sgtmelon.scriptum.parent.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.swipeDown
import sgtmelon.test.cappuccino.utils.withBackgroundDrawable
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Parent class for UI control [BlankMenuSheetDialog].
 */
abstract class SheetDialogPart(
    @IdRes containerId: Int,
    @IdRes navigationId: Int,
    @StringRes titleTextId: Int,
    @ArrayRes itemsId: Int
) : UiPart(),
    DialogUi {

    private val navigationContainer by lazy { getView(containerId) }
    private val navigationView by lazy { getView(navigationId) }
    private val titleText = getViewByText(titleTextId)

    private val buttonList = context.resources.getStringArray(itemsId).map { getViewByText(it) }

    fun getButton(value: Enum<*>): Matcher<View> = buttonList[value.ordinal]

    fun swipeClose() = waitClose { navigationView.swipeDown() }

    fun assert() {
        navigationContainer.isDisplayed().withBackgroundDrawable(R.drawable.bg_sheet_dialog)
        navigationView.isDisplayed()
        titleText.isDisplayed().withTextColor(R.attr.clContentSecond)

        for (button in buttonList) {
            button.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        }
    }
}