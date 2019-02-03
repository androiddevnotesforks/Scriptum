package sgtmelon.iconanim.widget

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import sgtmelon.iconanim.library.IconAnimUtil

/**
 * Версия [SwitchButton] с анимацией иконок при их смене
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class SwitchButtonAnim(context: Context, attrs: AttributeSet) : SwitchButton(context, attrs) {

    private val iconAnimUtil: IconAnimUtil?

    private val drawableDisableAnim: AnimatedVectorDrawable?
    private val drawableSelectAnim: AnimatedVectorDrawable?

    init {
        if (srcDisableAnim != SRC_NULL) {
            drawableDisableAnim = ContextCompat.getDrawable(getContext(), srcDisableAnim)
                    as AnimatedVectorDrawable?
            drawableDisableAnim?.setColorFilter(srcDisableColor, PorterDuff.Mode.SRC_ATOP)
        } else {
            drawableDisableAnim = null
        }

        if (srcSelectAnim != SRC_NULL) {
            drawableSelectAnim = ContextCompat.getDrawable(getContext(), srcSelectAnim)
                    as AnimatedVectorDrawable?
            drawableSelectAnim!!.setColorFilter(srcSelectColor, PorterDuff.Mode.SRC_ATOP)
        } else {
            drawableSelectAnim = null
        }

        iconAnimUtil = if (drawableSelectAnim != null && drawableDisableAnim != null) {
            IconAnimUtil(getContext(), drawableSelectAnim, drawableDisableAnim, this)
        } else null

    }

    override fun setDrawable(drawableOn: Boolean, needAnim: Boolean) {
        if (!needAnim) {
            setImageDrawable(if (drawableOn)
                drawableSelect
            else
                drawableDisable)
        } else if (iconAnimUtil != null) {
            iconAnimUtil.animState = drawableOn
            if (drawableOn) {
                setImageDrawable(iconAnimUtil.animOn)
                iconAnimUtil.animOn.start()
            } else {
                setImageDrawable(iconAnimUtil.animOff)
                iconAnimUtil.animOff.start()
            }
            iconAnimUtil.waitAnimationEnd()
        }
    }

}