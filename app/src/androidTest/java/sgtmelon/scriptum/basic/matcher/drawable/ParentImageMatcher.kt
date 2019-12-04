package sgtmelon.scriptum.basic.matcher.drawable

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.IdRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Parent class for matchers which need to compare images.
 */
abstract class ParentImageMatcher(@IdRes protected val resourceId: Int) : TypeSafeMatcher<View>() {

    private var viewWidth = 1
    private var viewHeight = 1

    /**
     * Need when drawable without size.
     */
    protected fun setSize(view: View) {
        viewWidth = view.width
        viewHeight = view.height
    }

    override fun describeTo(description: Description?) {
        description?.appendText("\nView with drawable from resource id = $resourceId")
    }

    protected fun compare(actual: Drawable, expected: Drawable): Boolean {
        return getBitmap(actual).sameAs(getBitmap(expected))
    }

    /**
     * Create bitmap from [drawable].
     */
    private fun getBitmap(drawable: Drawable): Bitmap = with(drawable) {
        val width = if (intrinsicWidth > 0) intrinsicWidth else viewWidth
        val height = if (intrinsicHeight > 0) intrinsicHeight else viewHeight

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)

        return bitmap
    }

}