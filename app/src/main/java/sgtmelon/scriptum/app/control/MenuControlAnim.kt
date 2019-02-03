package sgtmelon.scriptum.app.control

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.view.View
import android.view.Window

import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import sgtmelon.iconanim.office.hdlr.AnimHandler
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.utils.ColorUtils

/**
 * Класс для контроля меню с использованием анимации | Для версий API >= 21
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MenuControlAnim(context: Context,
                      window: Window,
                      toolbar: Toolbar,
                      indicator: View
) : MenuControl(context, window, toolbar, indicator) {

    private val cancelOnAnim = ColorUtils.getDrawable(
            context, R.drawable.anim_cancel_enter, R.attr.clContent
    ) as AnimatedVectorDrawable?

    private val cancelOffAnim = ColorUtils.getDrawable(
            context, R.drawable.anim_cancel_exit, R.attr.clContent
    ) as AnimatedVectorDrawable?

    private val animHandler: AnimHandler = AnimHandler(context, cancelOnAnim!!, cancelOffAnim!!)

    init {
        animHandler.setAnimation(this)
    }

    override fun setDrawable(drawableOn: Boolean, needAnim: Boolean) {
        if (!needAnim) {
            toolbar.navigationIcon = if (drawableOn) cancelOn
            else cancelOff
        } else {
            animHandler.setAnimState(drawableOn)
            if (drawableOn) {
                toolbar.navigationIcon = animHandler.animOn
                animHandler.startAnimOn()
            } else {
                toolbar.navigationIcon = animHandler.animOff
                animHandler.startAnimOff()
            }
            animHandler.waitAnimationEnd()
        }
    }

}