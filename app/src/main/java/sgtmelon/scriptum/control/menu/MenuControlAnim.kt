package sgtmelon.scriptum.control.menu

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import sgtmelon.iconanim.IconAnimControl
import sgtmelon.iconanim.IconBlockCallback
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
        indicator: View?,
        blockCallback: IconBlockCallback
) : MenuControl(theme, context, window, toolbar, indicator) {

    private val cancelEnterIcon = context.getTintDrawable(R.drawable.anim_cancel_enter)
            as? AnimatedVectorDrawable

    private val cancelExitIcon = context.getTintDrawable(R.drawable.anim_cancel_exit)
            as? AnimatedVectorDrawable

    private val iconAnimControl: IconAnimControl = IconAnimControl(
            context, cancelEnterIcon, cancelExitIcon, changeCallback = this
    ).apply { this.blockCallback = blockCallback }

    override fun setDrawable(enterIcon: Boolean, needAnim: Boolean) {
        if (!needAnim) {
            super.setDrawable(enterIcon, needAnim)
        } else {
            iconAnimControl.animState = enterIcon

            toolbar?.navigationIcon = if (enterIcon) {
                iconAnimControl.enterIcon?.apply { start() }
            } else {
                iconAnimControl.exitIcon?.apply { start() }
            }

            iconAnimControl.waitAnimationEnd()
        }
    }

}