package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import kotlin.math.min
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.widget.SwitchButton
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.infrastructure.adapter.RankAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.callback.click.RankClickListener
import sgtmelon.test.prod.RunNone

/**
 * Holder for category items - [RankItem], use inside [RankAdapter].
 */
// TODO add view binding
class RankHolder(itemView: View) : ParentHolder(itemView),
    UnbindCallback {

    private val clickView: View = itemView.findViewById(R.id.rank_click_container)
    private val visibleButton: SwitchButton = itemView.findViewById(R.id.rank_visible_button)

    private val nameText: TextView = itemView.findViewById(R.id.rank_name_text)
    private val imageContainer: ViewGroup = itemView.findViewById(R.id.rank_image_container)

    private val notificationContainer: ViewGroup =
        itemView.findViewById(R.id.rank_notification_container)
    private val notificationText: TextView = itemView.findViewById(R.id.rank_notification_text)
    private val bindContainer: ViewGroup = itemView.findViewById(R.id.rank_bind_container)
    private val bindText: TextView = itemView.findViewById(R.id.rank_bind_text)
    private val countText: TextView = itemView.findViewById(R.id.rank_text_count_text)

    private val cancelButton: ImageButton = itemView.findViewById(R.id.rank_cancel_button)

    fun bind(
        item: RankItem,
        dragListener: ItemListener.Drag,
        blockCallback: IconBlockCallback,
        callback: RankClickListener
    ) {
        updateContent(item)

        visibleButton.setBlockCallback(blockCallback)

        val touchListener = View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                dragListener.setDrag(v.id == clickView.id)
            }

            return@OnTouchListener false
        }

        clickView.setOnTouchListener(touchListener)
        cancelButton.setOnTouchListener(touchListener)
        visibleButton.setOnTouchListener(touchListener)

        clickView.setOnClickListener { checkNoPosition { callback.onRankClick(it) } }
        cancelButton.setOnClickListener { checkNoPosition { callback.onRankCancelClick(it) } }

        visibleButton.setDrawable(item.isVisible, needAnim = false)
        visibleButton.setOnClickListener { v ->
            checkNoPosition {
                callback.onRankVisibleClick(it) {
                    /**
                     * It's important to update item, because adapter notify
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

        notifyIndicators(item)

        val cancelDescription = context.getString(R.string.description_item_rank_cancel, item.name)
        cancelButton.contentDescription = cancelDescription
    }

    private fun notifyIndicators(item: RankItem) {
        val context = itemView.context
        val isNotificationVisible = isMaxTest || item.notificationCount != 0
        val isBindVisible = isMaxTest || item.bindCount != 0

        imageContainer.visibility = if (isNotificationVisible || isBindVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }

        notificationContainer.visibility = if (isNotificationVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
        notificationText.text = if (isMaxTest) {
            INDICATOR_MAX_COUNT.toString()
        } else {
            min(item.notificationCount, INDICATOR_MAX_COUNT).toString()
        }

        bindContainer.visibility = if (isBindVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
        bindText.text = if (isMaxTest) {
            INDICATOR_MAX_COUNT.toString()
        } else {
            min(item.bindCount, INDICATOR_MAX_COUNT).toString()
        }

        countText.text = context.getString(R.string.list_item_rank_count, item.noteId.size)
    }

    override fun unbind() {
        clickView.setOnTouchListener(null)
        cancelButton.setOnTouchListener(null)
        visibleButton.setOnTouchListener(null)

        clickView.setOnClickListener(null)
        cancelButton.setOnClickListener(null)
        visibleButton.setOnClickListener(null)
    }

    companion object {
        const val INDICATOR_MAX_COUNT = 99

        /**
         * Variable only for UI tests.
         */
        @RunNone var isMaxTest = false
    }
}