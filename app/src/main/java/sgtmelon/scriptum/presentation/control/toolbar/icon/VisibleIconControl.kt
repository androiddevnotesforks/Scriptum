package sgtmelon.scriptum.presentation.control.toolbar.icon

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.MenuItem
import sgtmelon.iconanim.IconAnimControl
import sgtmelon.iconanim.IconBlockCallback
import sgtmelon.iconanim.IconChangeCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment

/**
 * Class for control toolbar icon changes in [RollNoteFragment].
 */
class VisibleIconControl(
    context: Context,
    @Theme private val theme: Int,
    private val menuItem: MenuItem?,
    callback: IconBlockCallback
) : IconChangeCallback {

    protected val activeTint = if (theme == Theme.DARK) R.attr.clAccent else R.attr.clContent

    private val visibleEnter: Drawable? = context.getTintDrawable(R.drawable.ic_visible_enter)
    private val visibleExit: Drawable? = context.getTintDrawable(R.drawable.ic_visible_exit, activeTint)

    private val visibleEnterIcon = context.getTintDrawable(R.drawable.anim_visible_enter)
            as? AnimatedVectorDrawable

    private val visibleExitIcon = context.getTintDrawable(R.drawable.anim_visible_exit, activeTint)
            as? AnimatedVectorDrawable

    private val iconAnimControl: IconAnimControl = IconAnimControl(
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