package sgtmelon.scriptum.presentation.dialog.sheet

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import sgtmelon.safedialog.dialog.parent.BlankMenuSheetDialog
import sgtmelon.scriptum.R

/**
 * Sheet dialog for select note type for create.
 */
class AddSheetDialog : BlankMenuSheetDialog() {
    @get:LayoutRes override val layoutId: Int = R.layout.view_sheet_add
    @get:IdRes override val navigationId: Int = R.id.add_navigation
}