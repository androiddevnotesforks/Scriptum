package sgtmelon.scriptum.app.adapter.holder

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.iconanim.library.SwitchButton
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RankAdapter
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.databinding.ItemRankBinding
import sgtmelon.scriptum.office.intf.ItemIntf

/**
 * Держатель категори для [RankAdapter]
 */
@SuppressLint("ClickableViewAccessibility")
class RankHolder(private val binding: ItemRankBinding,
                 private val clickListener: ItemIntf.ClickListener,
                 private val longClickListener: ItemIntf.LongClickListener,
                 private val dragListener: ItemIntf.DragListener) : RecyclerView.ViewHolder(binding.root),
        View.OnTouchListener {

    private val clickView: View = itemView.findViewById(R.id.click_container)
    private val cancelButton: ImageButton = itemView.findViewById(R.id.cancel_button)

    val visibleButton: SwitchButton = itemView.findViewById(R.id.visible_button)

    init {
        clickView.setOnClickListener { v -> clickListener.onItemClick(v, adapterPosition) }
        cancelButton.setOnClickListener { v -> clickListener.onItemClick(v, adapterPosition) }

        visibleButton.setOnClickListener { v ->
            visibleButton.setDrawable(!binding.rankItem?.isVisible!!, true)
            clickListener.onItemClick(v, adapterPosition)
        }

        visibleButton.setOnLongClickListener { v ->
            longClickListener.onItemLongClick(v, adapterPosition)
        }

        clickView.setOnTouchListener(this)
        cancelButton.setOnTouchListener(this)
        visibleButton.setOnTouchListener(this)
    }

    fun bind(rankItem: RankItem) {
        binding.rankItem = rankItem
        binding.executePendingBindings()
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            dragListener.setDrag(v.id == R.id.click_container)
        }
        return false
    }

}