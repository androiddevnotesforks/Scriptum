package sgtmelon.iconanim.widget

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import sgtmelon.iconanim.IconAnimControl

/**
 * Version of [SwitchButton] with icon animation on switch
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class SwitchButtonAnim(context: Context, attrs: AttributeSet) : SwitchButton(context, attrs) {

    private val iconDisableAnim = if (srcDisableAnim != ND_SRC) {
        ContextCompat.getDrawable(context, srcDisableAnim) as? AnimatedVectorDrawable
    } else {
        null
    }

    private val iconSelectAnim = if (srcSelectAnim != ND_SRC) {
        ContextCompat.getDrawable(context, srcSelectAnim) as? AnimatedVectorDrawable
    } else {
        null
    }

    private val iconAnimControl: IconAnimControl = IconAnimControl(
            context, iconSelectAnim, iconDisableAnim, callback = this
    )

    init {
        iconDisableAnim?.setColorFilter(srcDisableColor, PorterDuff.Mode.SRC_ATOP)
        iconSelectAnim?.setColorFilter(srcSelectColor, PorterDuff.Mode.SRC_ATOP)
    }

    override fun setDrawable(drawableOn: Boolean, needAnim: Boolean) {
        if (!needAnim) {
            super.setDrawable(drawableOn, needAnim)
        } else {
            iconAnimControl.animState = drawableOn

            setImageDrawable(if (drawableOn) {
                iconAnimControl.iconOn?.apply { start() }
            } else {
                iconAnimControl.iconOff?.apply { start() }
            })

            iconAnimControl.waitAnimationEnd()
        }
    }

}