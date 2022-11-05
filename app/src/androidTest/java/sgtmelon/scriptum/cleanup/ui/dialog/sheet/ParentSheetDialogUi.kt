package sgtmelon.scriptum.cleanup.ui.dialog.sheet

import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import sgtmelon.safedialog.dialog.parent.BlankMenuSheetDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.IDialogUi
import sgtmelon.scriptum.ui.testing.screen.parent.ParentScreen
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.swipeDown
import sgtmelon.test.cappuccino.utils.withBackgroundDrawable

/**
 * Parent class for UI control [BlankMenuSheetDialog].
 */
abstract class ParentSheetDialogUi(
    @IdRes containerId: Int,
    @IdRes navigationId: Int
) : ParentScreen(), IDialogUi {

    private val navigationContainer = getViewById(containerId)
    private val navigationView = getViewById(navigationId)

    fun onCloseSwipe() = waitClose { navigationView.swipeDown() }

    @CallSuper open fun assert() {
        navigationContainer.isDisplayed().withBackgroundDrawable(R.drawable.bg_sheet_dialog)
        navigationView.isDisplayed()
    }
}