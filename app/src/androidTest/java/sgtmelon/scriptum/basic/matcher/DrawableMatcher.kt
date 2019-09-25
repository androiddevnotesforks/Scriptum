package sgtmelon.scriptum.basic.matcher

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import sgtmelon.scriptum.extension.getColorAttr


/**
 * Matcher for check android:src
 *
 * If [resourceId] is -1 => check what don't have drawable
 * If [attrColor] is -1 => check without colorFilter
 */
class DrawableMatcher(private val resourceId: Int, @AttrRes private val attrColor: Int) :
        TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("with drawable from resource id: ")
        description?.appendValue(resourceId)
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) return false

        if (resourceId < 0) return item.drawable == null

        val context = item.context

        val drawable = ContextCompat.getDrawable(context, resourceId) ?: return false

        if (attrColor != -1) {
            drawable.setColorFilter(context.getColorAttr(attrColor), PorterDuff.Mode.SRC_ATOP)
        }

        return getBitmap(item.drawable).sameAs(getBitmap(drawable))
    }

    private fun getBitmap(drawable: Drawable): Bitmap = with(drawable) {
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)

        return bitmap
    }


}