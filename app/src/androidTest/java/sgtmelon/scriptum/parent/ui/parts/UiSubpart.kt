package sgtmelon.scriptum.parent.ui.parts

import android.view.View
import androidx.annotation.IdRes
import org.hamcrest.Matcher
import sgtmelon.test.cappuccino.utils.isDescendant

/**
 * Basic UI element which placed inside [parentContainer].
 */
abstract class UiSubpart(protected val parentContainer: Matcher<View>) : UiPart() {

    override fun getView(@IdRes viewId: Int): Matcher<View> {
        return super.getView(viewId).isDescendant(parentContainer)
    }
}