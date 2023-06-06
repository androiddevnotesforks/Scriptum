package sgtmelon.safedialog.utils

import androidx.fragment.app.DialogFragment

/**
 * Class for decrease code copy-paste. Helps to init dialog, find and correctly restore it after
 * rotation.
 */
class DialogStorage<T: DialogFragment>(
    private val create: () -> T,
    private val find: () -> T?,
    private val setup: (T) -> Unit
) {

    /** Hold instance of dialog, while phone rotate not happen or [release] called. */
    private var dialog: T? = null

    /** Restore dialog after rotation. */
    fun restore() {
        if (dialog == null) {
            find()?.let {
                setup(it)
                dialog = it
            }
        }
    }

    fun release() {
        dialog = null
    }

    fun show(tag: String, owner: DialogOwner, prepare: T.() -> Unit = {}) {
        dialog?.safeDismiss(owner)
        dialog = create().apply(prepare).also {
            setup(it)
            it.safeShow(tag, owner)
        }
    }
}