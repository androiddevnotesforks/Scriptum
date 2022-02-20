package sgtmelon.iconanim.control

import android.graphics.drawable.AnimatedVectorDrawable
import sgtmelon.iconanim.IconAnimControl
import sgtmelon.iconanim.IconBlockCallback

/**
 * Interface for communicate with [IconAnimControl]
 */
interface IIconAnimControl {

    var blockCallback: IconBlockCallback?

    fun getIcon(isEnterIcon: Boolean): AnimatedVectorDrawable?
}