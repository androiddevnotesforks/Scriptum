package sgtmelon.scriptum.parent.ui.parts

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag

/**
 * Tag needed for correctly determinate views (because have same id's, like parent_container).
 */
abstract class ContainerPart(
    @TestViewTag private val tag: String,
    @IdRes private val parentId: Int = R.id.parent_container
) : UiPart() {

    protected val parentContainer = getParentView(parentId)

    private fun getParentView(@IdRes id: Int): Matcher<View> {
        return allOf(withId(id), withTagValue(`is`(tag)))
    }

    override fun getView(@IdRes viewId: Int): Matcher<View> {
        return allOf(super.getView(viewId), isDescendantOfA(parentContainer))
    }
}