package sgtmelon.test.cappuccino.matchers.drawable

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Parent class for matchers which need to compare images.
 */
abstract class ParentImageMatcher(
    protected val resourceId: Int?
) : TypeSafeMatcher<View>() {

    private var viewWidth = ND_SIZE
    private var viewHeight = ND_SIZE

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

    protected fun compare(expected: Drawable, actual: Drawable): Boolean {
        return getBitmap(expected).sameAs(getBitmap(actual))
    }

    /**
     * Create bitmap from [drawable].
     */
    private fun getBitmap(drawable: Drawable): Bitmap = with(drawable) {
        val width = if (viewWidth != ND_SIZE) viewWidth else intrinsicWidth
        val height = if (viewHeight != ND_SIZE) viewHeight else intrinsicHeight

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)

        return bitmap
    }

    companion object {
        private const val ND_SIZE = 1
    }
}