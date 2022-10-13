package sgtmelon.scriptum.infrastructure.adapter.callback.click

/**
 * Callback for catch rank events.
 */
interface RankClickListener {

    fun onRankVisibleClick(p: Int, onAction: () -> Unit)

    fun onRankClick(p: Int)

    fun onRankCancelClick(p: Int)
}