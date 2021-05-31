package sgtmelon.scriptum.basic.matcher.card

import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.cardview.widget.CardView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher for [CardView].
 */
class CardElevationMatcher(@DimenRes private val elevationId: Int) : TypeSafeMatcher<View>() {

    @Dimension private var expected: Float? = null
    @Dimension private var actual: Float? = null

    override fun matchesSafely(item: View?): Boolean {
        if (item !is CardView) return false

        val context = item.context ?: return false

        expected = context.resources.getDimension(elevationId)
        actual = item.cardElevation

        return expected == actual
    }

    override fun describeTo(description: Description?) {
        description?.appendText(
            "Expected elevation: $expected | actual elevation: $actual"
        )
    }
}