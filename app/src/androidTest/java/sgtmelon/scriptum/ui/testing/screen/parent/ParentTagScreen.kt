package sgtmelon.scriptum.ui.testing.screen.parent

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import sgtmelon.scriptum.R

abstract class ParentTagScreen(private val tag: String?) {

    protected val parentContainer = getParentView(R.id.parent_container)

    private fun getParentView(@IdRes id: Int): Matcher<View> {
        return if (tag == null) {
            withId(id)
        } else {
            allOf(withId(id), withTagValue(`is`(tag)))
        }
    }

    protected fun getView(@IdRes id: Int): Matcher<View> {
        return allOf(withId(id), isDescendantOfA(parentContainer))
    }
}