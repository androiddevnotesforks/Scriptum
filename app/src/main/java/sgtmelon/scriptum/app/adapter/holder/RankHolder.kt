package sgtmelon.scriptum.app.adapter.holder

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.iconanim.widget.SwitchButton
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RankAdapter
import sgtmelon.scriptum.app.model.item.RankItem
import sgtmelon.scriptum.databinding.ItemRankBinding
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.utils.AppUtils.checkNoPosition

/**
 * Держатель категори для [RankAdapter]
 */
@SuppressLint("ClickableViewAccessibility")
class RankHolder(private val binding: ItemRankBinding,
                 private val clickListener: ItemIntf.ClickListener,
                 private val longClickListener: ItemIntf.LongClickListener,
                 private val dragListener: ItemIntf.DragListener
) : RecyclerView.ViewHolder(binding.root),
        View.OnTouchListener {

    private val clickView: View = itemView.findViewById(R.id.rank_click_container)
    private val cancelButton: ImageButton = itemView.findViewById(R.id.rank_cancel_button)
    private val visibleButton: SwitchButton = itemView.findViewById(R.id.rank_visible_button)

    init {
        clickView.setOnClickListener { v ->
            checkNoPosition { clickListener.onItemClick(v, adapterPosition) }
        }
        cancelButton.setOnClickListener { v ->
            checkNoPosition { clickListener.onItemClick(v, adapterPosition) }
        }

        visibleButton.setOnClickListener { v ->
            checkNoPosition {
                visibleButton.setDrawable(!binding.rankItem?.isVisible!!, true)
                clickListener.onItemClick(v, adapterPosition)
            }
        }

        visibleButton.setOnLongClickListener { v ->
            checkNoPosition { longClickListener.onItemLongClick(v, adapterPosition) }
        }

        clickView.setOnTouchListener(this)
        cancelButton.setOnTouchListener(this)
        visibleButton.setOnTouchListener(this)
    }

    fun bind(rankItem: RankItem, startAnim: Boolean) {
        visibleButton.setDrawable(rankItem.isVisible, startAnim)

        binding.rankItem = rankItem
        binding.executePendingBindings()
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            dragListener.setDrag(v.id == clickView.id)
        }
        return false
    }

}