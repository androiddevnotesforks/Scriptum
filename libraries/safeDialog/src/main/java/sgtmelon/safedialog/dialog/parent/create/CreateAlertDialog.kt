package sgtmelon.safedialog.dialog.parent.create

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.content.Context

/**
 * Base class for alert dialogs.
 */
interface CreateAlertDialog : CreateDialog {

    var themeId: Int?

    override fun createDialog(context: Context): Dialog {
        return buildDialog(getBuilder(context), context).create()
    }

    private fun getBuilder(context: Context): AlertDialog.Builder {
        val themeId = themeId

        return if (themeId == null) {
            AlertDialog.Builder(context)
        } else {
            AlertDialog.Builder(context, themeId)
        }
    }

    /** Setup dialog [builder] inside realization of this function. */
    fun buildDialog(builder: AlertDialog.Builder, context: Context): AlertDialog.Builder

}