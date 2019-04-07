package sgtmelon.scriptum.app.adapter

import android.content.Context
import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.RankHolder
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.app.screen.view.main.RankFragment
import sgtmelon.scriptum.databinding.ItemRankBinding
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.AppUtils.inflateBinding

/**
 * Адаптер для [RankFragment]
 */
class RankAdapter(context: Context) : ParentAdapter<RankItem, RankHolder>(context) {

    // TODO убрать в конструктор

    lateinit var clickListener: ItemListener.ClickListener
    lateinit var longClickListener: ItemListener.LongClickListener
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