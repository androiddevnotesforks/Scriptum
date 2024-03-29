package sgtmelon.test.cappuccino.matchers.drawable

import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import org.hamcrest.Description
import sgtmelon.extensions.getColorAttr
import sgtmelon.extensions.getColorCompat
import sgtmelon.extensions.getDrawableCompat
import sgtmelon.extensions.setColorFilterCompat

/**
 * Matcher for check android:src which gets with [resourceId].
 *
 * If [resourceId] is null => check what don't have drawable.
 * If [colorId]/[attrColor] is null => check without colorFilter.
 */
class DrawableMatcher(
    @IdRes resourceId: Int?,
    @ColorRes private val colorId: Int?,
    @AttrRes private val attrColor: Int?
) : ParentImageMatcher(resourceId) {

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) return false

        if (resourceId == null) return item.drawable == null

        val context = item.context ?: return false
        val expected = context.getDrawableCompat(resourceId)?.mutate() ?: return false
        val actual = item.drawable

        when {
            colorId != null -> expected.setColorFilterCompat(context.getColorCompat(colorId))
            attrColor != null -> expected.setColorFilterCompat(context.getColorAttr(attrColor))
        }

        //         when {
        //            colorId != null -> DrawableCompat.setTint(expected, context.getColorCompat(colorId))
        //            attrColor != null -> DrawableCompat.setTint(expected, context.getColorAttr(attrColor))
        //        }

        /**
         * Vector may have different sizes (which declared not in xml), so need remember it and
         * set size across [setSize].
         *
         * TODO check on wide screen devices. It sometimes not work.
         */
        //        if (expected is VectorDrawable || actual is VectorDrawable) {
        //            setSize(item)
        //        }

        return compare(expected, actual)
    }

    override fun describeTo(description: Description?) {
        super.describeTo(description)

        if (colorId != null) {
            description?.appendText("\nView with colorId = $colorId")
        }

        if (attrColor != null) {
            description?.appendText("\nView with attrColor = $attrColor")
        }
    }

}