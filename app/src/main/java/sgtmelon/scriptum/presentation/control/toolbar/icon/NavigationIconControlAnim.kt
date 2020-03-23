package sgtmelon.scriptum.presentation.control.toolbar.icon

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import sgtmelon.iconanim.IconAnimControl
import sgtmelon.iconanim.IconBlockCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getTintDrawable

/**
 * Version of [NavigationIconControl] with icon animation on switch.
 *
 * Use only for API version >= 21
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NavigationIconControlAnim(
        context: Context,
        toolbar: Toolbar?,
        blockCallback: IconBlockCallback
): NavigationIconControl(context, toolbar) {

    private val cancelEnterIcon = context.getTintDrawable(R.drawable.anim_cancel_enter)
            as? AnimatedVectorDrawable

    private val cancelExitIcon = context.getTintDrawable(R.drawable.anim_cancel_exit)
            as? AnimatedVectorDrawable

    private val iconAnimControl: IconAnimControl = IconAnimControl(
            context, cancelEnterIcon, cancelExitIcon, changeCallback = this
    ).apply { this.blockCallback = blockCallback }

    override fun setDrawable(isEnterIcon: Boolean, needAnim: Boolean) {
        if (needAnim) {
            toolbar?.navigationIcon = iconAnimControl.getIcon(isEnterIcon)
        } else {
            super.setDrawable(isEnterIcon, needAnim)
        }
    }

}