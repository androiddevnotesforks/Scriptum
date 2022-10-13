package sgtmelon.scriptum.infrastructure.adapter.callback.click

interface RankClickListener {

    fun onRankVisibleClick(p: Int, onAction: () -> Unit)

    fun onRankClick(p: Int)

    fun onRankCancelClick(p: Int)
}