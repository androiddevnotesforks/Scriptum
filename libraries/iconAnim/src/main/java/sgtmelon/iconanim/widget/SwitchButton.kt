package sgtmelon.iconanim.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageButton
import sgtmelon.extensions.getDrawableCompat
import sgtmelon.extensions.setColorFilterCompat
import sgtmelon.iconanim.R
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.iconanim.control.AnimatedIcon

/**
 * Button with automatic icon change via [setDrawable] func.
 */
class SwitchButton(
    context: Context,
    attrs: AttributeSet
) : AppCompatImageButton(context, attrs),
    IconChangeCallback {

    @DrawableRes private val srcDisable: Int
    @DrawableRes private val srcSelect: Int
    @DrawableRes private val srcDisableAnim: Int
    @DrawableRes private val srcSelectAnim: Int

    @ColorInt private val srcDisableColor: Int
    @ColorInt private val srcSelectColor: Int

    private val iconDisable: Drawable?
    private val iconSelect: Drawable?
    private val iconDisableAnim: AnimatedVectorDrawable?
    private val iconSelectAnim: AnimatedVectorDrawable?

    private val animControl: AnimatedIcon

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)

        srcDisable = array.getResourceId(R.styleable.SwitchButton_srcDisable, ND_SRC)
        srcSelect = array.getResourceId(R.styleable.SwitchButton_srcSelect, ND_SRC)

        srcDisableAnim = array.getResourceId(R.styleable.SwitchButton_srcDisableAnim, ND_SRC)
        srcSelectAnim = array.getResourceId(R.styleable.SwitchButton_srcSelectAnim, ND_SRC)

        srcDisableColor = array.getColor(R.styleable.SwitchButton_srcDisableColor, Color.BLACK)
        srcSelectColor = array.getColor(R.styleable.SwitchButton_srcSelectColor, Color.BLACK)

        array.recycle()

        iconDisable = if (srcDisable != ND_SRC) context.getDrawableCompat(srcDisable) else null
        iconSelect = if (srcSelect != ND_SRC) context.getDrawableCompat(srcSelect) else null

        iconDisableAnim = if (srcDisableAnim != ND_SRC) {
            context.getDrawableCompat(srcDisableAnim) as? AnimatedVectorDrawable
        } else {
            null
        }
        iconSelectAnim = if (srcSelectAnim != ND_SRC) {
            context.getDrawableCompat(srcSelectAnim) as? AnimatedVectorDrawable
        } else {
            null
        }

        animControl = AnimatedIcon(context, iconSelectAnim, iconDisableAnim, changeCallback = this)
    }

    init {
        iconDisable?.setColorFilterCompat(srcDisableColor)
        iconSelect?.setColorFilterCompat(srcSelectColor)
        iconDisableAnim?.setColorFilterCompat(srcDisableColor)
        iconSelectAnim?.setColorFilterCompat(srcSelectColor)
    }

    fun setBlockCallback(blockCallback: IconBlockCallback) {
        animControl.setBlockCallback(blockCallback)
    }

    override var isEnterIcon: Boolean? = null

    override fun setDrawable(isEnterIcon: Boolean, needAnim: Boolean) {
        this.isEnterIcon = isEnterIcon

        val icon = if (needAnim) {
            animControl.getAndStart(isEnterIcon)
        } else {
            if (isEnterIcon) iconSelect else iconDisable
        }

        setImageDrawable(icon)
    }

    companion object {
        const val ND_SRC = -1
    }
}