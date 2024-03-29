package sgtmelon.safedialog.dialog.parent

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import sgtmelon.safedialog.dialog.parent.create.CreateDialog
import sgtmelon.safedialog.utils.safeShow

/**
 * Base class for safe dialogs without title/buttons/etc. Use mainly for [BlankButtonDialog] and custom
 * dialogs with view.
 */
abstract class BlankEmptyDialog : DialogFragment(),
    CreateDialog {

    /** For example, detect dialog rotation case. */
    protected var isContentRestored: Boolean = false
        private set

    var dismissListener: DialogInterface.OnDismissListener? = null

    inline fun onDismiss(crossinline func: () -> Unit) {
        dismissListener = DialogInterface.OnDismissListener { func() }
    }

    //region Dialog creation

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        if (savedInstanceState != null) {
            onRestoreContentState(savedInstanceState)
        }

        onRestoreArgumentState(bundle = savedInstanceState ?: arguments)


        val dialog = createDialog(requireContext())
        return transformDialog(dialog)
    }

    //endregion

    /**
     * Use for restore dialog content which was written before [safeShow]
     * (e.g. title, nameArray and etc.).
     *
     * Call inside [onCreateDialog] before create them.
     */
    @CallSuper
    protected open fun onRestoreContentState(savedState: Bundle) {
        isContentRestored = true
    }

    /**
     * Function for restore content which was passed through setArgument function
     * (e.g. position, check and etc.).
     *
     * Call inside [onCreateDialog] before create them.
     */
    @CallSuper
    protected open fun onRestoreArgumentState(bundle: Bundle?) = Unit

    override fun onStart() {
        super.onStart()
        setupView()
    }

    /** Func for setup child some view's of custom view. */
    @CallSuper
    protected open fun setupView() = Unit

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(dialog)
    }
}