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
import sgtmelon.iconanim.callback.ParentIconChange
import sgtmelon.iconanim.control.AnimatedIcon

/**
 * Button with automatic icon change via [setDrawable] func.
 */
class SwitchButton(
    context: Context,
    attrs: AttributeSet
) : AppCompatImageButton(context, attrs),
    ParentIconChange {

    @DrawableRes private val srcSelect: Int
    @DrawableRes private val srcDisable: Int
    @DrawableRes private val srcSelectAnim: Int
    @DrawableRes private val srcDisableAnim: Int

    @ColorInt private val srcSelectColor: Int
    @ColorInt private val srcDisableColor: Int

    private val iconSelect: Drawable?
    private val iconDisable: Drawable?
    private val iconSelectAnim: AnimatedVectorDrawable?
    private val iconDisableAnim: AnimatedVectorDrawable?

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)

        srcSelect = array.getResourceId(R.styleable.SwitchButton_srcSelect, ND_SRC)
        srcDisable = array.getResourceId(R.styleable.SwitchButton_srcDisable, ND_SRC)

        srcSelectAnim = array.getResourceId(R.styleable.SwitchButton_srcSelectAnim, ND_SRC)
        srcDisableAnim = array.getResourceId(R.styleable.SwitchButton_srcDisableAnim, ND_SRC)

        srcSelectColor = array.getColor(R.styleable.SwitchButton_srcSelectColor, Color.BLACK)
        srcDisableColor = array.getColor(R.styleable.SwitchButton_srcDisableColor, Color.BLACK)

        array.recycle()

        iconSelect = if (srcSelect != ND_SRC) context.getDrawableCompat(srcSelect) else null
        iconDisable = if (srcDisable != ND_SRC) context.getDrawableCompat(srcDisable) else null

        iconSelectAnim = if (srcSelectAnim != ND_SRC) {
            context.getDrawableCompat(srcSelectAnim) as? AnimatedVectorDrawable
        } else {
            null
        }
        iconDisableAnim = if (srcDisableAnim != ND_SRC) {
            context.getDrawableCompat(srcDisableAnim) as? AnimatedVectorDrawable
        } else {
            null
        }
    }

    init {
        iconSelect?.setColorFilterCompat(srcSelectColor)
        iconDisable?.setColorFilterCompat(srcDisableColor)
        iconSelectAnim?.setColorFilterCompat(srcSelectColor)
        iconDisableAnim?.setColorFilterCompat(srcDisableColor)
    }

    override val animatedIcon = AnimatedIcon(
        context, iconSelectAnim, iconDisableAnim, changeCallback = this
    )

    fun setBlockCallback(blockCallback: IconBlockCallback) {
        animatedIcon.setBlockCallback(blockCallback)
    }

    override val enterIcon: Drawable? = iconSelect
    override val exitIcon: Drawable? = iconDisable

    override var isEnterIcon: Boolean? = null

    override fun setDrawableAfterChange(drawable: Drawable?) = setImageDrawable(drawable)

    companion object {
        const val ND_SRC = -1
    }
}