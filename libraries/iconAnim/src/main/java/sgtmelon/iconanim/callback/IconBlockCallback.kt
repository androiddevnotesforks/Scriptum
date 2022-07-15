package sgtmelon.iconanim.callback

/**
 * Callback for help block button while animation run (prevent anim lags).
 */
interface IconBlockCallback {

    fun setEnabled(isEnabled: Boolean)
}