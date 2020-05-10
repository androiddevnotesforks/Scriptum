package sgtmelon.scriptum.presentation.adapter

import android.view.ViewGroup
import sgtmelon.iconanim.IconBlockCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.presentation.adapter.diff.RankDiff
import sgtmelon.scriptum.presentation.adapter.holder.RankHolder
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment

/**
 * Adapter which displays list of ranks for [RankFragment].
 */
class RankAdapter(
        private val blockCallback: IconBlockCallback,
        private val clickListener: ItemListener.ActionClick,
        private val longClickListener: ItemListener.LongClick
) : ParentDiffAdapter<RankItem, RankDiff, RankHolder>() {

    var dragListener: ItemListener.Drag? = null

    var startAnim: BooleanArray = BooleanArray(size = 0)


    override val diff = RankDiff()

    override fun setList(list: List<RankItem>) = apply {
        super.setList(list)

        this.list.clearAdd(ArrayList(list.map { it.copy() }))
        this.startAnim = BooleanArray(list.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankHolder {
        return RankHolder(
                parent.inflateBinding(R.layout.item_rank),
                clickListener, longClickListener, dragListener,
                blockCallback
        )
    }

    override fun onBindViewHolder(holder: RankHolder, position: Int) {
        holder.bind(list[position], startAnim[position])

        if (startAnim[position]) startAnim[position] = false
    }

}