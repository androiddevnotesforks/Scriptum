package sgtmelon.scriptum.basic.matcher.drawable

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.google.android.material.shape.MaterialShapeDrawable
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import sgtmelon.scriptum.extension.getColorAttr
import sgtmelon.scriptum.extension.getCompatColor

/**
 * Matcher for check background color.
 */
class BackgroundColorMatcher(
        @ColorRes private val colorId: Int?,
        @AttrRes private val attrColor: Int?
): TypeSafeMatcher<View>() {

    init {
        if (colorId == null && attrColor == null) throw IllegalAccessException()
    }

    private var index = 0
    @ColorInt private var expectedColor: Int? = null
    @ColorInt private var actualColor: Int? = null

    override fun matchesSafely(item: View?): Boolean {
        if (item == null) return false

        index = 1
        val context = item.context ?: return false
        val color = when {
            colorId != null -> context.getCompatColor(colorId)
            attrColor != null -> context.getColorAttr(attrColor)
            else -> throw IllegalAccessException()
        }

        index = 2
        val backgroundColor = when (val background = item.background) {
            is ColorDrawable -> background.color
            is MaterialShapeDrawable -> background.fillColor?.defaultColor
            else -> null
        } ?: return false

        index = 3
        expectedColor = color
        actualColor = backgroundColor

        return color == backgroundColor
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nIndex: $index")

        if (colorId != null) {
            description?.appendText("\nView with background colorId: $colorId")
        }

        if (attrColor != null) {
            description?.appendText("\nView with background attrColor: $attrColor")
        }

        description?.appendText("\nExpected color: $expectedColor\nActual color: $actualColor")
    }

}