package sgtmelon.scriptum.app.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.RankHolder
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.view.fragment.RankFragment
import sgtmelon.scriptum.databinding.ItemRankBinding

/**
 * Адаптер для [RankFragment]
 */
class RankAdapter(context: Context) : ParentAdapter<RankItem, RankHolder>(context) {

    var startAnim: BooleanArray? = null

    override fun setList(list: List<RankItem>) {
        super.setList(list)
        startAnim = BooleanArray(list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankHolder {
        val binding = DataBindingUtil.inflate<ItemRankBinding>(
                inflater, R.layout.item_rank, parent, false
        )
        return RankHolder(binding, dragListener)
    }

    override fun onBindViewHolder(holder: RankHolder, position: Int) {
        val item = list[position]

        holder.bind(item)

        holder.clickView.setOnClickListener { v -> clickListener.onItemClick(v, position) }

        holder.visibleButton.setDrawable(item.isVisible, startAnim!![position])
        holder.visibleButton.setOnClickListener { v ->
            holder.visibleButton.setDrawable(!item.isVisible, true)
            clickListener.onItemClick(v, position)
        }
        holder.visibleButton.setOnLongClickListener { v ->
            longClickListener.onItemLongClick(v, position)
        }

        holder.cancelButton.setOnClickListener { v -> clickListener.onItemClick(v, position) }

        if (startAnim!![position]) {
            startAnim!![position] = false
        }
    }

}