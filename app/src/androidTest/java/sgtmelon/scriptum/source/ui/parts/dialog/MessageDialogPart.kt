package sgtmelon.scriptum.source.ui.parts.dialog

import androidx.annotation.AttrRes
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import junit.framework.Assert.assertNotNull
import sgtmelon.safedialog.annotation.MessageType
import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.source.ui.feature.DialogUi
import sgtmelon.scriptum.source.ui.parts.UiPart
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
    @StringRes negativeId: Int? = null,
    @StringRes neutralId: Int? = null
): UiPart(),
    DialogUi {

    constructor(
        @StringRes titleId: Int,
        @StringRes messageId: Int,
        type: MessageType
    ) : this(titleId, messageId, type.positiveButton, type.negativeButton, type.neutralButton)

    @get:AttrRes open val negativeAttr: Int = R.attr.clContentSecond
    @get:AttrRes open val neutralAttr: Int = R.attr.clAccent

    private val titleText = getViewByText(titleId)
    private val messageText = getViewByText(messageId)
    private val positiveButton = getViewByText(positiveId)
    private val negativeButton = negativeId?.let(::getViewByText)
    private val neutralButton = neutralId?.let(::getViewByText)

    fun positive() = waitClose {
        positiveButton.click()
        onPositiveResult()
    }

    open fun onPositiveResult() = Unit

    fun negative() = waitClose {
        assertNotNull(negativeButton)
        negativeButton?.click()
    }

    fun neutral() = waitClose {
        assertNotNull(neutralButton)
        neutralButton?.click()
    }

    @CallSuper open  fun assert() {
        titleText.isDisplayed().withTextColor(R.attr.clContent)
        messageText.isDisplayed().withTextColor(R.attr.clContent)

        positiveButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        negativeButton?.isDisplayed()?.isEnabled()?.withTextColor(negativeAttr)
        neutralButton?.isDisplayed()?.isEnabled()?.withTextColor(neutralAttr)
    }
}