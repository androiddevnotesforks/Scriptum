package sgtmelon.scriptum.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.diff.NoteDiff
import sgtmelon.scriptum.adapter.diff.RankDiff
import sgtmelon.scriptum.adapter.holder.RankHolder
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.screen.ui.main.RankFragment

/**
 * Adapter which displays list of ranks for [RankFragment]
 */
class RankAdapter(
        private val clickListener: ItemListener.ActionClick,
        private val longClickListener: ItemListener.LongClick
) : ParentAdapter<RankItem, RankHolder>() {

    var dragListener: ItemListener.Drag? = null

    var startAnim: BooleanArray = BooleanArray(size = 0)

    private val diff = RankDiff()

    override fun setList(list: List<RankItem>) {
        this.list.clearAndAdd(ArrayList(list.map { it.copy() }))
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

    fun notifyList(list: List<RankItem>) {
        diff.setList(this.list, list)

        val diffResult = DiffUtil.calculateDiff(diff)

        setList(list)
        diffResult.dispatchUpdatesTo(this)
    }

}