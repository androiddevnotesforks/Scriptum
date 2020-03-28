package sgtmelon.scriptum.presentation.control.toolbar.icon

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.view.MenuItem
import androidx.annotation.RequiresApi
import sgtmelon.iconanim.IconAnimControl
import sgtmelon.iconanim.IconBlockCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getTintDrawable

/**
 * Version of [VisibleIconControl] with icon animation on switch.
 *
 * Use only for API version >= 21
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class VisibleIconControlAnim(
        context: Context,
        menuItem: MenuItem?,
        blockCallback: IconBlockCallback
) : VisibleIconControl(context, menuItem) {

    private val visibleEnterIcon = context.getTintDrawable(R.drawable.anim_visible_enter)
            as? AnimatedVectorDrawable

    private val visibleExitIcon = context.getTintDrawable(R.drawable.anim_visible_exit, R.attr.clAccent)
            as? AnimatedVectorDrawable

    private val iconAnimControl: IconAnimControl = IconAnimControl(
            context, visibleEnterIcon, visibleExitIcon, changeCallback = this
    ).apply { this.blockCallback = blockCallback }

    override fun setDrawable(isEnterIcon: Boolean, needAnim: Boolean) {
        if (needAnim) {
            menuItem?.icon = iconAnimControl.getIcon(isEnterIcon)
        } else {
            super.setDrawable(isEnterIcon, needAnim)
        }
    }

}