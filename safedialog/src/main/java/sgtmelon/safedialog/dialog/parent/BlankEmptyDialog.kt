package sgtmelon.safedialog.dialog.parent

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import sgtmelon.safedialog.utils.safeShow

/**
 * Base class for safe dialogs without title/buttons/etc. (only custom view). Use mainly for
 * [BlankDialog] and custom dialogs with view.
 */
abstract class BlankEmptyDialog : DialogFragment() {

    var dismissListener: DialogInterface.OnDismissListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
            onRestoreContentState(savedInstanceState)
        }

        return super.onCreateDialog(savedInstanceState)
    }

    /**
     * Use for restore dialog content which was written before [safeShow]
     * (e.g. title, nameArray and ect.).
     *
     * Call inside [onCreateDialog] before create them.
     */
    @CallSuper
    protected open fun onRestoreContentState(savedState: Bundle) = Unit

    override fun onStart() {
        super.onStart()
        setupView()
    }

    /**
     * Func for setup child some view's of custom view
     */
    @CallSuper protected open fun setupView() = Unit

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(dialog)
    }
}