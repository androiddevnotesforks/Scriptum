package sgtmelon.scriptum.control.menu

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import sgtmelon.iconanim.IconAnimControl
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Version of [MenuControl] with icon animation on switch
 *
 * Use only for API version >= 21
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MenuControlAnim(
        @Theme theme: Int,
        context: Context,
        window: Window,
        toolbar: Toolbar?,
        indicator: View?
) : MenuControl(theme, context, window, toolbar, indicator) {

    private val cancelOnAnim = context.getTintDrawable(R.drawable.anim_cancel_enter)
            as? AnimatedVectorDrawable

    private val cancelOffAnim = context.getTintDrawable(R.drawable.anim_cancel_exit)
            as? AnimatedVectorDrawable

    private val iconAnimControl: IconAnimControl = IconAnimControl(
            context, cancelOnAnim, cancelOffAnim, callback = this
    )

    override fun setDrawable(drawableOn: Boolean, needAnim: Boolean) {
        if (!needAnim) {
            super.setDrawable(drawableOn, needAnim)
        } else {
            iconAnimControl.animState = drawableOn

            toolbar?.navigationIcon = if (drawableOn) {
                iconAnimControl.iconOn?.apply { start() }
            } else {
                iconAnimControl.iconOff?.apply { start() }
            }

            iconAnimControl.waitAnimationEnd()
        }
    }

}