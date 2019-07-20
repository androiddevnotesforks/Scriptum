package sgtmelon.scriptum.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.RankHolder
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.ui.main.RankFragment

/**
 * Адаптер для [RankFragment]
 *
 * @author SerjantArbuz
 */
class RankAdapter(private val clickListener: ItemListener.Click,
                  private val longClickListener: ItemListener.LongClick
) : ParentAdapter<RankEntity, RankHolder>() {

    lateinit var dragListener: ItemListener.Drag

    var startAnim: BooleanArray = BooleanArray(size = 0)

    override fun setList(list: List<RankEntity>) {
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