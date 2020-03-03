package sgtmelon.scriptum.adapter.holder

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.iconanim.IconBlockCallback
import sgtmelon.iconanim.widget.SwitchButton
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.RankAdapter
import sgtmelon.scriptum.databinding.ItemRankBinding
import sgtmelon.scriptum.extension.checkNoPosition
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.item.RankItem

/**
 * Holder for rank, use in [RankAdapter]
 */
@SuppressLint("ClickableViewAccessibility")
class RankHolder(
        private val binding: ItemRankBinding,
        private val clickListener: ItemListener.ActionClick,
        private val longClickListener: ItemListener.LongClick,
        private val dragListener: ItemListener.Drag?,
        blockCallback: IconBlockCallback
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

        visibleButton.setBlockCallback(blockCallback)
        visibleButton.setOnClickListener { v ->
            checkNoPosition {
                clickListener.onItemClick(v, adapterPosition) {
                    val item = binding.item ?: return@onItemClick

                    /**
                     * It's important to update binding item, because adapter notify
                     * methods will not be called.
                     */
                    item.isVisible = !item.isVisible
                    visibleButton.setDrawable(item.isVisible, needAnim = true)

                    /**
                     * Need update item in binding after changes.
                     * Because notifyItemChanged will not happen (because of best anim performance).
                     */
                    bindItem(item)
                }
            }
        }

        visibleButton.setOnLongClickListener { v ->
            checkNoPosition { longClickListener.onItemLongClick(v, adapterPosition) }
            return@setOnLongClickListener true
        }

        clickView.setOnTouchListener(this)
        cancelButton.setOnTouchListener(this)
        visibleButton.setOnTouchListener(this)
    }

    fun bind(item: RankItem, startAnim: Boolean) {
        visibleButton.setDrawable(item.isVisible, startAnim)
        bindItem(item)
    }

    private fun bindItem(item: RankItem) = binding.apply {
        this.item = item
    }.executePendingBindings()

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            dragListener?.setDrag(v.id == clickView.id)
        }
        return false
    }

}