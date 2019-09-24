package sgtmelon.scriptum.basic.matcher

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


/**
 * Matcher for check android:src
 *
 * If [resourceId] is -1 => check what don't have drawable
 */
class DrawableMatcher(private val resourceId: Int = -1) : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("with drawable from resource id: ")
        description?.appendValue(resourceId)
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) return false

        if (resourceId < 0) return item.drawable == null

        val expectedDrawable = ContextCompat.getDrawable(item.context, resourceId) ?: return false

        return getBitmap(item.drawable).sameAs(getBitmap(expectedDrawable))
    }

    private fun getBitmap(drawable: Drawable): Bitmap = with(drawable) {
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)

        return bitmap
    }


}