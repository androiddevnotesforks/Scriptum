package sgtmelon.scriptum.source.ui.parts

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matcher
import org.junit.Assert.assertTrue
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.system.delegators.SnackbarDelegator
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

    fun action() = apply { actionButton.click() }

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
        /** 1 second for sure. */
        const val DISMISS_TIME = SnackbarDelegator.DISMISS_TIMEOUT + 1000L

        inline operator fun invoke(
            func: SnackbarPart.() -> Unit,
            @StringRes messageId: Int,
            @StringRes actionId: Int
        ): SnackbarPart {
            return SnackbarPart(messageId, actionId).apply(func)
        }
    }
}