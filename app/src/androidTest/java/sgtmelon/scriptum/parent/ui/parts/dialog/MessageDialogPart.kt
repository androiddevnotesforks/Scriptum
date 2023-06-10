package sgtmelon.scriptum.parent.ui.parts.dialog

import androidx.annotation.AttrRes
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import junit.framework.Assert.assertNotNull
import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.parent.ui.feature.DialogUi
import sgtmelon.scriptum.parent.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Parent class for UI control of [MessageDialog].
 */
abstract class MessageDialogPart(
    @StringRes titleId: Int,
    @StringRes messageId: Int,
    @StringRes positiveId: Int,
    @StringRes negativeId: Int,
    @StringRes neutralId: Int? = null
): UiPart(),
    DialogUi {

    @get:AttrRes open val negativeAttr: Int = R.attr.clContentSecond
    @get:AttrRes open val neutralAttr: Int = R.attr.clAccent

    private val titleText = getViewByText(titleId)
    private val messageText = getViewByText(messageId)
    private val positiveButton = getViewByText(positiveId)
    private val negativeButton = getViewByText(negativeId)
    private val neutralButton = neutralId?.let { getViewByText(it) }

    fun positive() = waitClose { positiveButton.click() }

    fun negative() = waitClose { negativeButton.click() }

    fun neutral() = waitClose {
        assertNotNull(neutralButton)
        neutralButton?.click()
    }

    @CallSuper open  fun assert() {
        titleText.isDisplayed().withTextColor(R.attr.clContent)
        messageText.isDisplayed().withTextColor(R.attr.clContent)

        positiveButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        negativeButton.isDisplayed().isEnabled().withTextColor(negativeAttr)
        neutralButton?.isDisplayed()?.isEnabled()?.withTextColor(neutralAttr)
    }
}