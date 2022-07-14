package sgtmelon.iconanim.control

import android.graphics.drawable.AnimatedVectorDrawable
import sgtmelon.iconanim.callback.IconBlockCallback

/**
 * Interface for communicate with [IconAnimControlImpl].
 */
interface IIconAnimControl {

    var blockCallback: IconBlockCallback?

    fun getIcon(isEnterIcon: Boolean): AnimatedVectorDrawable?
}