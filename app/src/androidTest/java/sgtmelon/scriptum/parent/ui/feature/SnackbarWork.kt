package sgtmelon.scriptum.parent.ui.feature

import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import sgtmelon.scriptum.parent.ui.parts.SnackbarPart
import sgtmelon.test.cappuccino.utils.await

/**
 * Abstraction for screens with ability to show [Snackbar].
 */
interface SnackbarWork {

    @get:StringRes val snackbarMessage: Int
    @get:StringRes val snackbarAction: Int

    /**
     * Be careful calling this function, because every time it will trigger [await] func
     * inside
     */
    fun snackbar(func: SnackbarPart.() -> Unit = {}): SnackbarPart {
        return SnackbarPart(snackbarMessage, snackbarAction, func)
    }

    fun assertSnackbarDismissed() = snackbar().assertDismiss()

}