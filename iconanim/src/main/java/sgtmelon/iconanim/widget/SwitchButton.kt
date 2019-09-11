package sgtmelon.iconanim.widget

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import sgtmelon.iconanim.IconCallback
import sgtmelon.iconanim.R

/**
 * Button with automatic icon change via [setDrawable]
 *
 * @author SerjantArbuz
 */
open class SwitchButton(context: Context, attrs: AttributeSet)
    : AppCompatImageButton(context, attrs),
        IconCallback {

    @DrawableRes protected val srcDisable: Int
    @DrawableRes protected val srcSelect: Int
    @DrawableRes protected val srcDisableAnim: Int
    @DrawableRes protected val srcSelectAnim: Int

    @ColorInt protected val srcDisableColor: Int
    @ColorInt protected val srcSelectColor: Int

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)

        srcDisable = array.getResourceId(R.styleable.SwitchButton_srcDisable, ND_SRC)
        srcSelect = array.getResourceId(R.styleable.SwitchButton_srcSelect, ND_SRC)

        srcDisableAnim = array.getResourceId(R.styleable.SwitchButton_srcDisableAnim, ND_SRC)
        srcSelectAnim = array.getResourceId(R.styleable.SwitchButton_srcSelectAnim, ND_SRC)

        srcDisableColor = array.getColor(R.styleable.SwitchButton_srcDisableColor, Color.BLACK)
        srcSelectColor = array.getColor(R.styleable.SwitchButton_srcSelectColor, Color.BLACK)

        array.recycle()
    }

    private val iconDisable: Drawable? =
            if (srcDisable != ND_SRC) ContextCompat.getDrawable(context, srcDisable) else null

    private val iconSelect: Drawable? =
            if (srcSelect != ND_SRC) ContextCompat.getDrawable(context, srcSelect) else null

    init {
        iconDisable?.setColorFilter(srcDisableColor, PorterDuff.Mode.SRC_ATOP)
        iconSelect?.setColorFilter(srcSelectColor, PorterDuff.Mode.SRC_ATOP)
    }

    override fun setDrawable(drawableOn: Boolean, needAnim: Boolean) {
        setImageDrawable(if (drawableOn) iconSelect else iconDisable)
    }

    companion object {
        const val ND_SRC = -1
    }

}