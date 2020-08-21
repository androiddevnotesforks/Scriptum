package sgtmelon.scriptum.presentation.control.snackbar

import android.view.ViewGroup
import sgtmelon.scriptum.domain.model.annotation.Theme

/**
 * Interface for [SnackbarControl].
 */
interface ISnackbarControl {

    fun show(parent: ViewGroup, @Theme theme: Int, withInsets: Boolean)

    fun dismiss()

}