package sgtmelon.scriptum.presentation.control.toolbar.icon

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.MenuItem
import sgtmelon.iconanim.control.IconAnimControlImpl
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback
import sgtmelon.iconanim.control.IIconAnimControl
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment

/**
 * Class for control toolbar icon changes in [RollNoteFragment].
 */
class VisibleIconControl(
    context: Context,
    private val menuItem: MenuItem?,
    callback: IconBlockCallback
) : IconChangeCallback {

    private val visibleEnter: Drawable? = context.getTintDrawable(R.drawable.ic_visible_enter)
    private val visibleExit: Drawable? = context.getTintDrawable(R.drawable.ic_visible_exit, R.attr.clIndicator)

    private val visibleEnterIcon = context.getTintDrawable(R.drawable.anim_visible_enter)
            as? AnimatedVectorDrawable
    private val visibleExitIcon = context.getTintDrawable(R.drawable.anim_visible_exit, R.attr.clIndicator)
            as? AnimatedVectorDrawable

    private val iconAnimControl: IIconAnimControl = IconAnimControlImpl(
        context, visibleEnterIcon, visibleExitIcon, changeCallback = this
    ).apply { this.blockCallback = callback }

    override fun setDrawable(isEnterIcon: Boolean, needAnim: Boolean) {
        if (needAnim) {
            menuItem?.icon = iconAnimControl.getIcon(isEnterIcon)
        } else {
            menuItem?.icon = if (isEnterIcon) visibleEnter else visibleExit
        }
    }
}