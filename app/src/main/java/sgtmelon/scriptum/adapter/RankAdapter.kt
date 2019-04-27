package sgtmelon.scriptum.adapter

import android.content.Context
import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.RankHolder
import sgtmelon.scriptum.databinding.ItemRankBinding
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding
import sgtmelon.scriptum.screen.view.main.RankFragment

/**
 * Адаптер для [RankFragment]
 *
 * @author SerjantArbuz
 */
class RankAdapter(context: Context,
                  val clickListener: ItemListener.ClickListener,
                  val longClickListener: ItemListener.LongClickListener
) : ParentAdapter<RankItem, RankHolder>(context) {

    lateinit var dragListener: ItemListener.DragListener

    var startAnim: BooleanArray = BooleanArray(size = 0)

    override fun setList(list: List<RankItem>) {
        super.setList(list)
        startAnim = BooleanArray(list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankHolder {
        val binding: ItemRankBinding = inflater.inflateBinding(R.layout.item_rank, parent)
        return RankHolder(binding, clickListener, longClickListener, dragListener)
    }

    override fun onBindViewHolder(holder: RankHolder, position: Int) {
        val item = list[position]

        holder.bind(item, startAnim[position])

        if (startAnim[position]) startAnim[position] = false
    }

}