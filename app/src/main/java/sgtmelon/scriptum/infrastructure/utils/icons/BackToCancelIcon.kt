package sgtmelon.scriptum.infrastructure.utils.icons

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.Toolbar
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.ParentIconChange
import sgtmelon.iconanim.control.AnimatedIcon
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.extensions.getTintDrawable

/**
 * Class for transform toolbar navigation icon: from [arrowIcon] to [cancelIcon].
 */
class BackToCancelIcon(
    context: Context,
    private val toolbar: Toolbar?,
    callback: IconBlockCallback
) : ParentIconChange {

    private val cancelIcon: Drawable? = context.getTintDrawable(R.drawable.ic_cancel_enter)
    private val arrowIcon: Drawable? = context.getTintDrawable(R.drawable.ic_cancel_exit)

    private val cancelEnterIcon = context.getTintDrawable(R.drawable.anim_cancel_enter)
            as? AnimatedVectorDrawable
    private val cancelExitIcon = context.getTintDrawable(R.drawable.anim_cancel_exit)
            as? AnimatedVectorDrawable

    override val animatedIcon = AnimatedIcon(
        context, cancelEnterIcon, cancelExitIcon, changeCallback = this, callback
    )

    override val enterIcon: Drawable? = cancelIcon
    override val exitIcon: Drawable? = arrowIcon

    override var isEnterIcon: Boolean? = null

    override fun setDrawableAfterChange(drawable: Drawable?) {
        toolbar?.navigationIcon = drawable
    }
}