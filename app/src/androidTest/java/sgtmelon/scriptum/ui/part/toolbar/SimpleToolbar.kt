package sgtmelon.scriptum.ui.part.toolbar

import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.withChild
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.withBackgroundAttr
import sgtmelon.scriptum.basic.extension.withNavigationDrawable

/**
 * Part of UI abstraction for describe simple toolbar.
 */
class SimpleToolbar(@StringRes titleId: Int, private val withBack: Boolean) : ParentToolbar() {

    val contentContainer: Matcher<View> = allOf(
            getViewById(R.id.toolbar_container), withChild(getViewByText(titleId))
    )

    fun assert() {
        val backIconDrawable = if (withBack) R.drawable.ic_cancel_exit else null
        val backIconTint = if (withBack) R.attr.clContent else null

        contentContainer.isDisplayed()
            .withBackgroundAttr(R.attr.colorPrimary)
            .withNavigationDrawable(backIconDrawable, backIconTint)
    }
}