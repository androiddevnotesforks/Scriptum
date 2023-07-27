package sgtmelon.scriptum.source.ui.parts.toolbar

import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withParent
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matcher
import sgtmelon.scriptum.source.ui.parts.UiSubpart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.withMenuDrawable
import sgtmelon.test.cappuccino.utils.withMenuTitle

/**
 * UI abstraction for toolbars.
 */
abstract class ToolbarPart(parentContainer: Matcher<View>) : UiSubpart(parentContainer) {

    abstract val toolbar: Matcher<View>

    private fun getToolbarButton(): Matcher<View> {
        val className = anyOf(
            `is`(ImageButton::class.java.name),
            `is`(AppCompatImageButton::class.java.name)
        )

        return allOf(
            withParent(toolbar),
            withClassName(className)
        )
    }

    fun clickButton() {
        getToolbarButton().click()
    }

    fun assertItem(item: ToolbarItem) = apply {
        item.assert()
        with(item) {
            toolbar.withMenuDrawable(itemId, iconId, tintAttr)
                .withMenuTitle(itemId, labelId)
        }
    }
}