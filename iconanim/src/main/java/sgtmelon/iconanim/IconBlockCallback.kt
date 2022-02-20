package sgtmelon.iconanim

/**
 * Callback for [IconAnimControl]. Help block button for prevent lags.
 */
interface IconBlockCallback {
    fun setEnabled(isEnabled: Boolean)
}