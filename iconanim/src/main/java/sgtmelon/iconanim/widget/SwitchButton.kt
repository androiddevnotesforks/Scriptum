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
import sgtmelon.iconanim.R
import sgtmelon.iconanim.library.IconAnimControl

/**
 * Кнопка с автоматизацией процесса смены иконки
 */
open class SwitchButton(internal val context: Context, attrs: AttributeSet)
    : AppCompatImageButton(context, attrs), IconAnimControl {

    @DrawableRes protected val srcDisable: Int
    @DrawableRes protected val srcSelect: Int
    @DrawableRes protected val srcDisableAnim: Int
    @DrawableRes protected val srcSelectAnim: Int

    @ColorInt protected val srcDisableColor: Int
    @ColorInt protected val srcSelectColor: Int

    protected val drawableDisable: Drawable?
    protected val drawableSelect: Drawable?

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)

        srcDisable = array.getResourceId(R.styleable.SwitchButton_srcDisable, SRC_NULL)
        srcSelect = array.getResourceId(R.styleable.SwitchButton_srcSelect, SRC_NULL)

        srcDisableAnim = array.getResourceId(R.styleable.SwitchButton_srcDisableAnim, SRC_NULL)
        srcSelectAnim = array.getResourceId(R.styleable.SwitchButton_srcSelectAnim, SRC_NULL)

        srcDisableColor = array.getColor(R.styleable.SwitchButton_srcDisableColor, Color.BLACK)
        srcSelectColor = array.getColor(R.styleable.SwitchButton_srcSelectColor, Color.BLACK)

        array.recycle()

        if (srcDisable != SRC_NULL) {
            drawableDisable = ContextCompat.getDrawable(context, srcDisable)
            drawableDisable?.setColorFilter(srcDisableColor, PorterDuff.Mode.SRC_ATOP)
        } else {
            drawableDisable = null
        }

        if (srcSelect != SRC_NULL) {
            drawableSelect = ContextCompat.getDrawable(context, srcSelect)
            drawableSelect?.setColorFilter(srcSelectColor, PorterDuff.Mode.SRC_ATOP)
        } else {
            drawableSelect = null
        }
    }

    override fun setDrawable(drawableOn: Boolean, needAnim: Boolean) {
        setImageDrawable(if (drawableOn) drawableSelect
        else drawableDisable)
    }

    companion object {
        const val SRC_NULL = -1
    }

}