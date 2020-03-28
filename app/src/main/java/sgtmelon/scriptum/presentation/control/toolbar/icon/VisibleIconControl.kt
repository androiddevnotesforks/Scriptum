package sgtmelon.scriptum.presentation.control.toolbar.icon

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.MenuItem
import sgtmelon.iconanim.IconChangeCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getTintDrawable

open class VisibleIconControl(
        protected val context: Context,
        protected val menuItem: MenuItem?
) : IconChangeCallback {

    private val visibleEnter: Drawable? = context.getTintDrawable(R.drawable.ic_visible_enter)
    private val visibleExit: Drawable? = context.getTintDrawable(R.drawable.ic_visible_exit, R.attr.clAccent)

    override fun setDrawable(isEnterIcon: Boolean, needAnim: Boolean) {
        menuItem?.icon = if (isEnterIcon) visibleEnter else visibleExit
    }

}