package sgtmelon.scriptum.parent.ui.feature

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import org.hamcrest.Matcher
import sgtmelon.scriptum.parent.ui.parts.SnackbarPart
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.getRandomPosition

/**
 * Abstraction for screens with ability to show [Snackbar].
 */
interface ListSnackbarWork {

    val recyclerView: Matcher<View>

    @get:StringRes val snackbarMessage: Int
    @get:StringRes val snackbarAction: Int

    /**
     * Be careful calling this function, because every time it will trigger [await] func
     * inside
     */
    fun snackbar(withAwait: Boolean = true, func: SnackbarPart.() -> Unit = {}) {
        SnackbarPart(func, snackbarMessage, snackbarAction, withAwait)
    }

    fun itemCancel(p: Int? = recyclerView.getRandomPosition(), isWait: Boolean = false)

    fun assertSnackbarDismissed() = snackbar(withAwait = false) { assertDismiss() }

}