package sgtmelon.test.cappuccino.matchers.card

import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.cardview.widget.CardView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for compare [CardView] radius.
 */
class CardRadiusMatcher(@DimenRes private val radiusId: Int) : TypeSafeMatcher<View>() {

    @Dimension private var expected: Float? = null
    @Dimension private var actual: Float? = null

    override fun matchesSafely(item: View?): Boolean {
        if (item !is CardView) return false

        val context = item.context ?: return false

        expected = context.resources.getDimension(radiusId)
        actual = item.radius

        return expected == actual
    }

    override fun describeTo(description: Description?) {
        description?.appendText("Expected radius: $expected | actual radius: $actual")
    }
}