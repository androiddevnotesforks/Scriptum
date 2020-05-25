package sgtmelon.scriptum.ui.part.panel

import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.withChild
import com.google.android.material.snackbar.Snackbar
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.waitBefore
import sgtmelon.scriptum.ui.ParentUi

/**
 * Part of UI abstraction for [Snackbar].
 */
class SnackbarPanel(
        @StringRes private val messageId: Int,
        @StringRes private val actionId: Int
) : ParentUi() {

    private val parentContainer: Matcher<View> = allOf(withChild(getViewByText(actionId)))

    private val messageText = getViewByText(messageId)
    private val actionButton = getViewByText(actionId)

    fun onClickCancel() = actionButton.click()

    // TODO more asserts
    fun assert() {
        parentContainer.isDisplayed()
        messageText.isDisplayed()
        actionButton.isDisplayed()
    }

    companion object {
        private const val UPDATE_TIME = 500L

        operator fun invoke(@StringRes messageId: Int, @StringRes actionId: Int,
                            func: SnackbarPanel.() -> Unit): SnackbarPanel {
            waitBefore(UPDATE_TIME)
            return SnackbarPanel(messageId, actionId).apply(func)
        }
    }

}