package sgtmelon.scriptum.source.ui.parts.toolbar

import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withChild
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withBackgroundAttr
import sgtmelon.test.cappuccino.utils.withNavigationDrawable
import sgtmelon.iconanim.R as animR

/**
 * UI abstraction of toolbars with button and title.
 */
class TitleToolbarPart(
    parentContainer: Matcher<View>,
    @StringRes titleId: Int,
    private val withBack: Boolean = true
) : ToolbarPart(parentContainer) {

    override val toolbar: Matcher<View> = allOf(
        getView(R.id.toolbar),
        withChild(getViewByText(titleId)),
        isDescendantOfA(parentContainer)
    )

    fun assert() = apply {
        val backDrawable = if (withBack) animR.drawable.ic_cancel_exit else null
        val backTint = if (withBack) R.attr.clContent else null

        toolbar.isDisplayed()
            .withBackgroundAttr(R.attr.colorPrimary)
            .withNavigationDrawable(backDrawable, backTint)
    }
}