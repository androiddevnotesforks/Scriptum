package sgtmelon.scriptum.cleanup.presentation.dialog.sheet

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import sgtmelon.safedialog.dialog.parent.BlankMenuSheetDialog
import sgtmelon.scriptum.R

/**
 * Sheet dialog for select alarm repeat.
 */
class RepeatSheetDialog : BlankMenuSheetDialog() {
    @get:LayoutRes override val layoutId: Int = R.layout.view_sheet_repeat
    @get:IdRes override val navigationId: Int = R.id.repeat_navigation
}