package sgtmelon.scriptum.basic.extension

import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import sgtmelon.scriptum.basic.matcher.layout.SizeCodeMatcher
import sgtmelon.scriptum.basic.matcher.layout.SizeMatcher

fun Matcher<View>.instanceOf(type: Class<*>) = also { matchOnView(it, Matchers.instanceOf(type)) }


fun Matcher<View>.withParent(parent: Matcher<View>) = also {
    matchOnView(it, ViewMatchers.withParent(parent))
}

/**
 * Need match if view is visible, otherwise will get exception.
 */
fun Matcher<View>.withSize(@DimenRes widthId: Int? = null,
                           @DimenRes heightId: Int? = null) = also {
    matchOnView(it, SizeMatcher(widthId, heightId, null, null))
}

/**
 * Need match if view is visible, otherwise will get exception.
 */
fun Matcher<View>.withSizeAttr(@AttrRes widthAttr: Int? = null,
                               @AttrRes heightAttr: Int? = null) = also {
    matchOnView(it, SizeMatcher(null, null, widthAttr, heightAttr))
}

/**
 * Need match if view is visible, otherwise will get exception.
 */
fun Matcher<View>.withSizeCode(width: Int? = null, height: Int? = null) = also {
    matchOnView(it, SizeCodeMatcher(width, height))
}