package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.inflateView
import sgtmelon.scriptum.cleanup.extension.safeSet
import sgtmelon.scriptum.cleanup.presentation.adapter.diff.RankDiff
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.RankHolder
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.RankFragment

/**
 * Adapter which displays list of ranks for [RankFragment].
 */
class RankAdapter(
    private val blockCallback: IconBlockCallback,
    private val clickListener: ItemListener.ActionClick,
    private val longClickListener: ItemListener.LongClick
) : ParentDiffAdapter<RankItem, RankDiff, RankHolder>() {

    var dragListener: ItemListener.Drag? = null

    var startAnimArray: BooleanArray = BooleanArray(size = 0)

    override val diff = RankDiff()

    override fun setList(list: List<RankItem>) = apply {
        super.setList(list)

        this.list.clearAdd(ArrayList(list.map { it.copy() }))
        this.startAnimArray = BooleanArray(list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankHolder {
        val itemView = parent.inflateView(R.layout.item_rank)
        return RankHolder(itemView, clickListener, longClickListener, dragListener, blockCallback)
    }

    override fun onBindViewHolder(holder: RankHolder, position: Int) {
        val item = list.getOrNull(position) ?: return
        val startAnim = startAnimArray.getOrNull(position) ?: return

        holder.bind(item, startAnim)

        if (startAnim) {
            startAnimArray.safeSet(position, false)
        }
    }

}