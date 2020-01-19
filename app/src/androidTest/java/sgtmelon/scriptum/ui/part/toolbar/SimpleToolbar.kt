package sgtmelon.scriptum.ui.part.toolbar

import android.os.Build
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers.withChild
import org.hamcrest.CoreMatchers.allOf
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.withBackgroundAttr
import sgtmelon.scriptum.basic.extension.withSize

/**
 * Part of UI abstraction for describe simple toolbar.
 */
class SimpleToolbar(@StringRes titleId: Int) : ParentToolbar() {

    private val toolbarContainer = allOf(
            getViewById(R.id.toolbar_container), withChild(getViewByText(titleId))
    )

    private val dividerView = getViewById(R.id.toolbar_divider_view)

    fun assert() {
        toolbarContainer.isDisplayed().withBackgroundAttr(R.attr.colorPrimary)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            dividerView.isDisplayed()
                    .withSize(heightId = R.dimen.layout_1dp)
                    .withBackgroundAttr(R.attr.clDivider)
        }
    }

}