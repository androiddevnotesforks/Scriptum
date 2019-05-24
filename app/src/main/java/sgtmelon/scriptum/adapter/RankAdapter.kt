package sgtmelon.scriptum.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.RankHolder
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.inflateBinding
import sgtmelon.scriptum.screen.view.main.RankFragment

/**
 * Адаптер для [RankFragment]
 *
 * @author SerjantArbuz
 */
class RankAdapter(private val clickListener: ItemListener.ClickListener,
                  private val longClickListener: ItemListener.LongClickListener
) : ParentAdapter<RankItem, RankHolder>() {

    lateinit var dragListener: ItemListener.DragListener

    var startAnim: BooleanArray = BooleanArray(size = 0)

    override fun setList(list: List<RankItem>) {
        super.setList(list)
        startAnim = BooleanArray(list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RankHolder(
            parent.inflateBinding(R.layout.item_rank),
            clickListener, longClickListener, dragListener
    )

    override fun onBindViewHolder(holder: RankHolder, position: Int) {
        holder.bind(list[position], startAnim[position])

        if (startAnim[position]) startAnim[position] = false
    }

}