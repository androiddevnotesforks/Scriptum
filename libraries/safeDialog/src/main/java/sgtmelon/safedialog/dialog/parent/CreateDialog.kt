package sgtmelon.safedialog.dialog.parent

import android.app.Dialog
import android.content.Context
import androidx.annotation.CallSuper
import sgtmelon.safedialog.utils.applyAnimation

/**
 * Interface with common function to create dialog and transform it after creation.
 */
interface CreateDialog {

    fun createDialog(context: Context): Dialog

    @CallSuper
    fun transformDialog(dialog: Dialog): Dialog = dialog.applyAnimation()

}