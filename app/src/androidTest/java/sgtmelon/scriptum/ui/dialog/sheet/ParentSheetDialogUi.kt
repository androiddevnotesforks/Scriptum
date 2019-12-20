package sgtmelon.scriptum.ui.dialog.sheet

import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import sgtmelon.safedialog.BlankMenuSheetDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi

/**
 * Parent class for UI control [BlankMenuSheetDialog].
 */
abstract class ParentSheetDialogUi(
        @IdRes containerId: Int,
        @IdRes navigationId: Int
) : ParentUi(), IDialogUi {

    private val navigationContainer = getViewById(containerId)
    private val navigationView = getViewById(navigationId)

    fun onCloseSwipe() = waitClose { navigationView.swipeDown() }

    @CallSuper open fun assert() {
        navigationContainer.isDisplayed().withBackground(when(theme == Theme.LIGHT) {
            true -> R.drawable.bg_dialog_light
            false -> R.drawable.bg_dialog_dark
        })

        navigationView.isDisplayed()
    }

}