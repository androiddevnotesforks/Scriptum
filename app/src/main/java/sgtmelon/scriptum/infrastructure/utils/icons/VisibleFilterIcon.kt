package sgtmelon.scriptum.infrastructure.utils.icons

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.MenuItem
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.ParentIconChange
import sgtmelon.iconanim.control.AnimatedIcon
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.extensions.getTintDrawable

/**
 * Class for transform [menuItem] icon: from [visibleEnter] to [visibleExit].
 */
class VisibleFilterIcon(
    context: Context,
    private val menuItem: MenuItem?,
    callback: IconBlockCallback
) : ParentIconChange {

    private val visibleEnter: Drawable?
    private val visibleExit: Drawable?

    init {
        visibleEnter = context.getTintDrawable(R.drawable.ic_visible_enter)
        visibleExit = context.getTintDrawable(R.drawable.ic_visible_exit, R.attr.clIndicator)
    }

    private val visibleEnterIcon: AnimatedVectorDrawable?
    private val visibleExitIcon: AnimatedVectorDrawable?

    init {
        visibleEnterIcon = context.getTintDrawable(R.drawable.anim_visible_enter)
                as? AnimatedVectorDrawable
        visibleExitIcon = context.getTintDrawable(R.drawable.anim_visible_exit, R.attr.clIndicator)
                as? AnimatedVectorDrawable
    }

    override val animatedIcon = AnimatedIcon(
        context, visibleEnterIcon, visibleExitIcon, changeCallback = this, callback
    )

    override val enterIcon: Drawable? = visibleEnter
    override val exitIcon: Drawable? = visibleExit

    override var isEnterIcon: Boolean? = null

    override fun setDrawableAfterChange(drawable: Drawable?) {
        menuItem?.icon = drawable
    }
}