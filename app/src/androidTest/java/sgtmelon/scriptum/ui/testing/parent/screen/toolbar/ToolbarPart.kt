package sgtmelon.scriptum.ui.testing.parent.screen.toolbar

import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withParent
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import sgtmelon.scriptum.ui.testing.parent.screen.UiPart
import sgtmelon.test.cappuccino.utils.click

/**
 * UI abstraction for toolbars.
 */
abstract class ToolbarPart : UiPart() {

    private fun getToolbarButton(): Matcher<View> {
        val className = anyOf(
            `is`(ImageButton::class.java.name),
            `is`(AppCompatImageButton::class.java.name)
        )

        return allOf(
            withParent(withClassName(`is`(Toolbar::class.java.name))),
            withClassName(className)
        )
    }

    fun clickButton() {
        getToolbarButton().click()
    }
}