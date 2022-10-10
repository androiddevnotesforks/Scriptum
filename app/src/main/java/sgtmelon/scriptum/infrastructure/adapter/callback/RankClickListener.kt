package sgtmelon.scriptum.infrastructure.adapter.callback

interface RankClickListener {

    fun onRankVisibleClick(p: Int, action: () -> Unit)

    fun onRankClick(p: Int)

    fun onRankCancelClick(p: Int)
}