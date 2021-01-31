package sgtmelon.scriptum.presentation.adapter.holder

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import sgtmelon.iconanim.IconBlockCallback
import sgtmelon.iconanim.widget.SwitchButton
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.presentation.adapter.RankAdapter
import sgtmelon.scriptum.presentation.listener.ItemListener
import kotlin.math.min

/**
 * Holder for rank, use in [RankAdapter].
 */
@SuppressLint("ClickableViewAccessibility")
class RankHolder(
    itemView: View,
    private val clickListener: ItemListener.ActionClick,
    private val longClickListener: ItemListener.LongClick,
    private val dragListener: ItemListener.Drag?,
    blockCallback: IconBlockCallback
) : ParentHolder(itemView),
    View.OnTouchListener {

    private val clickView: View = itemView.findViewById(R.id.rank_click_container)
    private val visibleButton: SwitchButton = itemView.findViewById(R.id.rank_visible_button)

    private val nameText: TextView = itemView.findViewById(R.id.rank_name_text)
    private val imageContainer: ViewGroup = itemView.findViewById(R.id.rank_image_container)

    private val notificationContainer: ViewGroup = itemView.findViewById(R.id.rank_notification_container)
    private val notificationText: TextView = itemView.findViewById(R.id.rank_notification_text)
    private val bindContainer: ViewGroup = itemView.findViewById(R.id.rank_bind_container)
    private val bindText: TextView = itemView.findViewById(R.id.rank_bind_text)
    private val countText: TextView = itemView.findViewById(R.id.rank_text_count_text)

    private val cancelButton: ImageButton = itemView.findViewById(R.id.rank_cancel_button)

    init {
        clickView.setOnClickListener { v -> checkNoPosition { clickListener.onItemClick(v, it) } }
        cancelButton.setOnClickListener { v ->
            checkNoPosition { clickListener.onItemClick(v, it) }
        }

        visibleButton.setBlockCallback(blockCallback)

        visibleButton.setOnLongClickListener { v ->
            checkNoPosition { longClickListener.onItemLongClick(v, it) }
        }

        clickView.setOnTouchListener(this)
        cancelButton.setOnTouchListener(this)
        visibleButton.setOnTouchListener(this)
    }

    fun bind(item: RankItem, startAnim: Boolean) {
        updateContent(item)

        visibleButton.setDrawable(item.isVisible, startAnim)
        visibleButton.setOnClickListener { v ->
            checkNoPosition {
                clickListener.onItemClick(v, it) {
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
                    updateContent(item)
                }
            }
        }
    }

    private fun updateContent(item: RankItem) {
        val context = itemView.context

        val visibleDescription = if (item.isVisible) {
            context.getString(R.string.description_item_rank_hide, item.name)
        } else {
            context.getString(R.string.description_item_rank_show, item.name)
        }
        visibleButton.contentDescription = visibleDescription

        nameText.text = item.name

        val isNotificationEmpty = item.notificationCount == 0
        val isBindEmpty = item.bindCount == 0
        imageContainer.visibility = if (!isNotificationEmpty || !isBindEmpty) {
            View.VISIBLE
        } else {
            View.GONE
        }
        notificationContainer.visibility = if (!isNotificationEmpty) View.VISIBLE else View.GONE
        notificationText.text = min(item.notificationCount, INDICATOR_MAX_COUNT).toString()
        bindContainer.visibility = if (!isBindEmpty) View.VISIBLE else View.GONE
        bindText.text = min(item.bindCount, INDICATOR_MAX_COUNT).toString()

        countText.text = context.getString(R.string.list_item_rank_count, item.noteId.size)

        val cancelDescription = context.getString(R.string.description_item_rank_cancel, item.name)
        cancelButton.contentDescription = cancelDescription

    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            dragListener?.setDrag(v.id == clickView.id)
        }
        return false
    }

    companion object {
        const val INDICATOR_MAX_COUNT = 99
    }
}