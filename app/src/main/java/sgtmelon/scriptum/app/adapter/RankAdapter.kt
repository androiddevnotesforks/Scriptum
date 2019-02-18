package sgtmelon.scriptum.app.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.RankHolder
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.ui.main.rank.RankFragment
import sgtmelon.scriptum.databinding.ItemRankBinding
import sgtmelon.scriptum.office.intf.ItemIntf

/**
 * Адаптер для [RankFragment]
 */
class RankAdapter(context: Context,
                  clickListener: ItemIntf.ClickListener,
                  private val longClickListener: ItemIntf.LongClickListener
) : ParentAdapter<RankItem, RankHolder>(context, clickListener) {

    lateinit var dragListener: ItemIntf.DragListener

    var startAnim: BooleanArray = BooleanArray(0)

    override fun setList(list: List<RankItem>) {
        super.setList(list)
        startAnim = BooleanArray(list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankHolder {
        val binding = DataBindingUtil.inflate<ItemRankBinding>(
                inflater, R.layout.item_rank, parent, false
        )
        return RankHolder(binding, clickListener, longClickListener, dragListener)
    }

    override fun onBindViewHolder(holder: RankHolder, position: Int) {
        val item = list[position]

        holder.bind(item, startAnim[position])

        if (startAnim[position]) startAnim[position] = false
    }

}