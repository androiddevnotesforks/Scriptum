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
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.extension.getTintDrawable

/**
 * Класс для контроля меню с использованием анимации | Для версий API >= 21
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MenuControlAnim(@Theme theme: Int,
                      context: Context,
                      window: Window,
                      toolbar: Toolbar?,
                      indicator: View?
) : MenuControl(theme, context, window, toolbar, indicator) {

    private val cancelOnAnim = context.getTintDrawable(R.drawable.anim_cancel_enter)
            as? AnimatedVectorDrawable?

    private val cancelOffAnim = context.getTintDrawable(R.drawable.anim_cancel_exit)
            as? AnimatedVectorDrawable?

    private val iconAnimControl: IconAnimControl = IconAnimControl(
            context, cancelOnAnim, cancelOffAnim, callback = this
    )

    override fun setDrawable(drawableOn: Boolean, needAnim: Boolean) {
        if (!needAnim) {
            toolbar?.navigationIcon = if (drawableOn) cancelOn else cancelOff
        } else {
            iconAnimControl.animState = drawableOn
            if (drawableOn) {
                toolbar?.navigationIcon = iconAnimControl.animOn
                iconAnimControl.animOn?.start()
            } else {
                toolbar?.navigationIcon = iconAnimControl.animOff
                iconAnimControl.animOff?.start()
            }
            iconAnimControl.waitAnimationEnd()
        }
    }

}