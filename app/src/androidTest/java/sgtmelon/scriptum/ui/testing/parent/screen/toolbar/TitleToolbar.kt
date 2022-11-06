package sgtmelon.scriptum.ui.testing.parent.screen.toolbar

import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.withChild
import org.hamcrest.Matchers.allOf
import sgtmelon.scriptum.R
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withBackgroundAttr
import sgtmelon.test.cappuccino.utils.withNavigationDrawable
import sgtmelon.iconanim.R as animR

/**
 * UI abstraction of toolbars with button and title.
 */
class TitleToolbar(
    @StringRes titleId: Int,
    private val withBack: Boolean = true
) : ToolbarPart() {

    private val toolbar = allOf(getView(R.id.toolbar), withChild(getViewByText(titleId)))

    fun assert() {
        val backDrawable = if (withBack) animR.drawable.ic_cancel_exit else null
        val backTint = if (withBack) R.attr.clContent else null

        toolbar.isDisplayed()
            .withBackgroundAttr(R.attr.colorPrimary)
            .withNavigationDrawable(backDrawable, backTint)
    }
}