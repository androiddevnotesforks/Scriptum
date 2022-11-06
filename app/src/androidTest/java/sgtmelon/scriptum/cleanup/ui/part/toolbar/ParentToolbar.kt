package sgtmelon.scriptum.cleanup.ui.part.toolbar

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
import sgtmelon.scriptum.cleanup.ui.ParentScreen

/**
 * Parent UI abstraction for toolbars
 */
abstract class ParentToolbar : ParentScreen() {

    fun getToolbarButton(): Matcher<View> = allOf(
        withParent(withClassName(`is`(Toolbar::class.java.name))),
        withClassName(
            anyOf(
                `is`(ImageButton::class.java.name),
                `is`(AppCompatImageButton::class.java.name)
            )
        )
    )

}