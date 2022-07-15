package sgtmelon.scriptum.cleanup.presentation.control.snackbar

import android.view.ViewGroup

/**
 * Interface for [SnackbarControl].
 */
interface ISnackbarControl {

    fun show(parent: ViewGroup, withInsets: Boolean)

    fun dismiss()

    fun dismiss(withCallback: Boolean)

}