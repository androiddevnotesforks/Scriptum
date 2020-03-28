package sgtmelon.scriptum.ui.part.toolbar

import android.os.Build
import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.withChild
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.withBackgroundAttr
import sgtmelon.scriptum.basic.extension.withNavigationDrawable
import sgtmelon.scriptum.basic.extension.withSize

/**
 * Part of UI abstraction for describe simple toolbar.
 */
class SimpleToolbar(@StringRes titleId: Int, private val withBack: Boolean) : ParentToolbar() {

    val contentContainer: Matcher<View> = allOf(
            getViewById(R.id.toolbar_container), withChild(getViewByText(titleId))
    )

    private val dividerView = getViewById(R.id.toolbar_divider_view)

    fun assert() {
        val backIconDrawable = if (withBack) R.drawable.ic_cancel_exit else -1
        val backIconTint = if (withBack) R.attr.clContent else -1

        contentContainer.isDisplayed()
                .withBackgroundAttr(R.attr.colorPrimary)
                .withNavigationDrawable(backIconDrawable, backIconTint)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            dividerView.isDisplayed()
                    .withSize(heightId = R.dimen.layout_1dp)
                    .withBackgroundAttr(R.attr.clDivider)
        }
    }

}