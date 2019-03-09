package sgtmelon.scriptum.app.control.menu

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.view.View
import android.view.Window

import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import sgtmelon.iconanim.library.IconAnimUtil
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.utils.ColorUtils.getDrawable

/**
 * Класс для контроля меню с использованием анимации | Для версий API >= 21
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MenuControlAnim(context: Context,
                      window: Window,
                      toolbar: Toolbar?,
                      indicator: View?
) : MenuControl(context, window, toolbar, indicator) {

    private val cancelOnAnim = context.getDrawable(R.drawable.anim_cancel_enter, R.attr.clContent)
            as AnimatedVectorDrawable?

    private val cancelOffAnim = context.getDrawable(R.drawable.anim_cancel_exit, R.attr.clContent)
            as AnimatedVectorDrawable?

    private val iconAnimUtil: IconAnimUtil = IconAnimUtil(
            context, cancelOnAnim, cancelOffAnim, this
    )

    override fun setDrawable(drawableOn: Boolean, needAnim: Boolean) {
        if (!needAnim) {
            toolbar?.navigationIcon = if (drawableOn) cancelOn else cancelOff
        } else {
            iconAnimUtil.animState = drawableOn
            if (drawableOn) {
                toolbar?.navigationIcon = iconAnimUtil.animOn
                iconAnimUtil.animOn?.start()
            } else {
                toolbar?.navigationIcon = iconAnimUtil.animOff
                iconAnimUtil.animOff?.start()
            }
            iconAnimUtil.waitAnimationEnd()
        }
    }

}