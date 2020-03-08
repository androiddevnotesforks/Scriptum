package sgtmelon.iconanim

/**
 * Interface for communicate with [IconAnimControl].
 */
interface IconBlockCallback {

    /**
     * Block button fow prevent lags.
     */
    fun setEnabled(enabled: Boolean)

}