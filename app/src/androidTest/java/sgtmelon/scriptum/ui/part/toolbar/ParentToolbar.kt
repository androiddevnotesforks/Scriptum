package sgtmelon.scriptum.ui.part.toolbar

import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withParent
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import sgtmelon.scriptum.ui.ParentUi

/**
 * Parent UI abstraction for toolbars
 */
abstract class ParentToolbar : ParentUi() {

    fun getToolbarButton(): Matcher<View> = allOf(
            withParent(withClassName(`is`(Toolbar::class.java.name))),
            withClassName(anyOf(
                    `is`(ImageButton::class.java.name),
                    `is`(AppCompatImageButton::class.java.name)
            ))
    )

}