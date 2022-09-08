package sgtmelon.test.cappuccino.utils

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import sgtmelon.test.cappuccino.matchers.layout.SizeCodeMatcher
import sgtmelon.test.cappuccino.matchers.layout.SizeMatcher

fun Matcher<View>.instanceOf(type: Class<*>) = also { matchOnView(it, Matchers.instanceOf(type)) }


fun Matcher<View>.withParent(parent: Matcher<View>) = also {
    matchOnView(it, ViewMatchers.withParent(parent))
}

/** Need match if view is visible, otherwise will get exception. */
fun Matcher<View>.withSize(
    @DimenRes widthId: Int? = null,
    @DimenRes heightId: Int? = null
): Matcher<View> {
    matchOnView(this, SizeMatcher(widthId, heightId, null, null))
    return this
}

/** Need match if view is visible, otherwise will get exception. */
fun Matcher<View>.withSizeAttr(
    @AttrRes widthAttr: Int? = null,
    @AttrRes heightAttr: Int? = null
): Matcher<View> {
    matchOnView(this, SizeMatcher(null, null, widthAttr, heightAttr))
    return this
}

/** Need match if view is visible, otherwise will get exception. */
fun Matcher<View>.withSizeCode(width: Int? = null, height: Int? = null): Matcher<View> {
    matchOnView(this, SizeCodeMatcher(width, height))
    return this
}