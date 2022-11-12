package sgtmelon.scriptum.parent.ui.parts

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matcher
import org.junit.Assert.assertTrue
import sgtmelon.scriptum.R
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withBackgroundDrawable
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Part of UI abstraction for [Snackbar].
 */
class SnackbarPart(
    @StringRes private val messageId: Int,
    @StringRes private val actionId: Int
) : UiPart() {

    private val parentContainer: Matcher<View> =
        allOf(instanceOf(Snackbar.SnackbarLayout::class.java))

    private val messageText = getViewByText(messageId)
    private val actionButton = getViewByText(actionId)

    fun clickCancel() = apply { actionButton.click() }

    fun assert() {
        parentContainer.isDisplayed().withBackgroundDrawable(R.drawable.bg_snackbar)
        messageText.isDisplayed().withTextColor(R.attr.clContent)
        actionButton.isDisplayed().withTextColor(R.attr.clAccent)
    }

    /**
     * Assert what snackbar was dismissed.
     */
    fun assertDismiss() {
        val result = try {
            assert()
            false
        } catch (e: Throwable) {
            true
        }

        assertTrue(result)
    }

    companion object {
        const val UPDATE_TIME = 300L
        const val DISMISS_TIME = 3000L

        inline operator fun invoke(
            func: SnackbarPart.() -> Unit,
            @StringRes messageId: Int,
            @StringRes actionId: Int,
            withAwait: Boolean
        ): SnackbarPart {
            if (withAwait) {
                await(UPDATE_TIME)
            }

            return SnackbarPart(messageId, actionId).apply(func)
        }
    }
}