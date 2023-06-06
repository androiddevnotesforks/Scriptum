package sgtmelon.safedialog.utils

import androidx.fragment.app.DialogFragment

/**
 * Class for decrease code copy-paste. Helps to init dialog, find and correctly restore it after
 * rotation.
 */
class DialogStorage<T: DialogFragment>(
    private val tag: String,
    private val owner: DialogOwner,
    private val create: () -> T,
    private val setup: (T) -> Unit
) {

    /** Hold instance of dialog, while phone rotate not happen or [release] called. */
    private var dialog: T? = null

    /** Restore dialog after rotation. */
    @Suppress("UNCHECKED_CAST")
    fun restore() {
        if (dialog == null) {
            (owner.fm.findFragmentByTag(tag) as? T)?.let {
                setup(it)
                dialog = it
            }
        }
    }

    fun release() {
        dialog = null
    }

    fun show(prepare: T.() -> Unit = {}) {
        dialog?.safeDismiss(owner)
        dialog = create().apply(prepare).also {
            setup(it)
            it.safeShow(tag, owner)
        }
    }
}