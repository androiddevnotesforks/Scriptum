package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.presentation.adapter.diff.RankDiff
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.RankHolder
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.infrastructure.adapter.ParentListAdapter
import sgtmelon.scriptum.infrastructure.utils.inflateView

/**
 * Adapter which displays list of categories (ranks).
 */
class RankAdapter(
    private val blockCallback: IconBlockCallback,
    private val clickListener: ItemListener.ActionClick,
) : ParentListAdapter<RankItem, RankHolder>(RankDiff()) {

    var dragListener: ItemListener.Drag? = null

    override fun getListCopy(list: List<RankItem>): List<RankItem> {
        return ArrayList(list.map { it.copy() })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankHolder {
        val itemView = parent.inflateView(R.layout.item_rank)
        return RankHolder(itemView, clickListener, dragListener, blockCallback)
    }

    override fun onBindViewHolder(holder: RankHolder, position: Int) {
        holder.bind(getItem(position))
    }
}