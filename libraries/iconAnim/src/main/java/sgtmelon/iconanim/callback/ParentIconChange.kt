package sgtmelon.iconanim.callback

import android.graphics.drawable.Drawable
import sgtmelon.iconanim.control.AnimatedIcon

interface ParentIconChange : IconChangeCallback {

    val animatedIcon: AnimatedIcon

    val enterIcon: Drawable?
    val exitIcon: Drawable?

    override var isEnterIcon: Boolean?

    override fun setDrawable(isEnterIcon: Boolean, needAnim: Boolean) {
        /** If tries setup the same icon state - it will be skipped. */
        if (this.isEnterIcon == isEnterIcon) return

        this.isEnterIcon = isEnterIcon

        val icon = if (needAnim) {
            animatedIcon.getAndStart(isEnterIcon)
        } else {
            if (isEnterIcon) enterIcon else exitIcon
        }

        setDrawableAfterChange(icon)
    }

    /**
     * In this function realization need set drawable to UI element, where icon is.
     */
    fun setDrawableAfterChange(drawable: Drawable?)

}