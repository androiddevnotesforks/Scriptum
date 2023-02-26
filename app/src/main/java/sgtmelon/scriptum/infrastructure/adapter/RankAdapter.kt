package sgtmelon.scriptum.infrastructure.adapter

import android.view.ViewGroup
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.infrastructure.adapter.callback.click.RankClickListener
import sgtmelon.scriptum.infrastructure.adapter.diff.RankDiff
import sgtmelon.scriptum.infrastructure.adapter.holder.RankHolder
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentDiffAdapter
import sgtmelon.scriptum.infrastructure.adapter.touch.listener.ItemDragListener
import sgtmelon.scriptum.infrastructure.utils.extensions.inflateBinding

/**
 * Adapter which displays list of [RankItem]'s.
 */
class RankAdapter(
    private val dragListener: ItemDragListener,
    private val blockCallback: IconBlockCallback,
    private val callback: RankClickListener
) : ParentDiffAdapter<RankItem, RankHolder>(RankDiff()) {

    override fun getListCopy(list: List<RankItem>): List<RankItem> {
        return ArrayList(list.map { it.copy() })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankHolder {
        return RankHolder(parent.inflateBinding(R.layout.item_rank))
    }

    override fun onBindViewHolder(holder: RankHolder, position: Int) {
        holder.bind(getItem(position), dragListener, blockCallback, callback)
    }
}