package sgtmelon.scriptum.cleanup.ui.part.panel

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.isDisplayed
import sgtmelon.scriptum.cleanup.basic.extension.waitBefore
import sgtmelon.scriptum.cleanup.basic.extension.withTextColor
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.test.cappuccino.utils.click

/**
 * Part of UI abstraction for [Snackbar].
 */
class SnackbarPanel(
        @StringRes private val messageId: Int,
        @StringRes private val actionId: Int
) : ParentUi() {

    private val parentContainer: Matcher<View> = allOf(instanceOf(Snackbar.SnackbarLayout::class.java))

    private val messageText = getViewByText(messageId)
    private val actionButton = getViewByText(actionId)

    fun onClickCancel() = actionButton.click()


    // TODO add assertion for background (i try but nothing happened)
    fun assert() {
        parentContainer.isDisplayed()
//                .withBackground(when (theme) {
//                    Theme.LIGHT -> R.drawable.bg_snackbar_light
//                    Theme.DARK -> R.drawable.bg_snackbar_dark
//                    else -> throw NullPointerException()
//                })

        messageText.isDisplayed().withTextColor(R.attr.clContent)
        actionButton.isDisplayed().withTextColor(R.attr.clAccent)
    }

    companion object {
        private const val UPDATE_TIME = 500L

        operator fun invoke(
            @StringRes messageId: Int,
            @StringRes actionId: Int,
            func: SnackbarPanel.() -> Unit
        ): SnackbarPanel {
            waitBefore(UPDATE_TIME)
            return SnackbarPanel(messageId, actionId).apply(func)
        }
    }
}