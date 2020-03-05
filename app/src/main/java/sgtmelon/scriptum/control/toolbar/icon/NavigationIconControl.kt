package sgtmelon.scriptum.control.toolbar.icon

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.Toolbar

import sgtmelon.iconanim.IconChangeCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment

/**
 * Class for control toolbar navigation icon changes in [TextNoteFragment], [RollNoteFragment].
 */
open class NavigationIconControl(
        protected val context: Context,
        protected val toolbar: Toolbar?
) : IconChangeCallback {

    private val cancelIcon: Drawable? = context.getTintDrawable(R.drawable.ic_cancel_enter)
    private val arrowIcon: Drawable? = context.getTintDrawable(R.drawable.ic_cancel_exit)

    override fun setDrawable(isEnterIcon: Boolean, needAnim: Boolean) {
        toolbar?.navigationIcon = if (isEnterIcon) cancelIcon else arrowIcon
    }

}