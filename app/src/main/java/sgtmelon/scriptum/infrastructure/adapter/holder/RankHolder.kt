package sgtmelon.scriptum.infrastructure.adapter.holder

import android.annotation.SuppressLint
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.databinding.ItemRankBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.ItemDragListener
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.callback.click.RankClickListener
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.adapter.touch.DragTouchListener
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible
import sgtmelon.test.prod.RunNone

@SuppressLint("ClickableViewAccessibility")
class RankHolder(
    private val binding: ItemRankBinding
) : ParentHolder(binding.root),
    UnbindCallback {

    fun bind(
        item: RankItem,
        dragListener: ItemDragListener,
        blockCallback: IconBlockCallback,
        callback: RankClickListener
    ) = with(binding) {
        visibleButton.setBlockCallback(blockCallback)

        val touchListener = DragTouchListener(dragListener, clickContainer)
        clickContainer.setOnTouchListener(touchListener)
        cancelButton.setOnTouchListener(touchListener)
        visibleButton.setOnTouchListener(touchListener)

        clickContainer.setOnClickListener { checkPosition { callback.onRankClick(it) } }
        cancelButton.setOnClickListener { checkPosition { callback.onRankCancelClick(it) } }
        visibleButton.setOnClickListener { checkPosition { onVisibleClick(callback, item, it) } }

        bindContent(item, withAnim = false)
    }

    private fun bindContent(item: RankItem, withAnim: Boolean) = with(binding) {
        val visibleDescription = if (item.isVisible) {
            context.getString(R.string.desc_rank_hide, item.name)
        } else {
            context.getString(R.string.desc_rank_show, item.name)
        }
        visibleButton.contentDescription = visibleDescription
        visibleButton.setDrawable(item.isVisible, withAnim)

        nameText.text = item.name

        notifyIndicators(item)

        val cancelDescription = context.getString(R.string.desc_rank_cancel, item.name)
        cancelButton.contentDescription = cancelDescription
    }

    private fun notifyIndicators(item: RankItem) = with(binding) {
        val isAlarmVisible = isMaxTest || item.notificationCount != 0
        val isBindVisible = isMaxTest || item.bindCount != 0

        imageContainer.makeVisible(condition = isAlarmVisible || isBindVisible)

        notificationContainer.makeVisible(isAlarmVisible)
        notificationText.text = getIndicatorCount(item.notificationCount)

        bindContainer.makeVisible(isBindVisible)
        bindText.text = getIndicatorCount(item.bindCount)

        countText.text = context.getString(R.string.list_rank_count, item.noteId.size)
    }

    private fun getIndicatorCount(count: Int): String {
        return when {
            isMaxTest -> MAX_COUNT_TEXT
            count > MAX_COUNT -> MAX_COUNT_TEXT
            else -> count.toString()
        }
    }

    private fun onVisibleClick(callback: RankClickListener, item: RankItem, p: Int) {
        callback.onRankVisibleClick(p) {
            /**
             * It's important to update item here, because adapter notify func will not
             * be called (related with anim performance).
             */
            item.isVisible = !item.isVisible

            /** Need update item data after changes (notify func will not be called). */
            bindContent(item, withAnim = true)
        }
    }

    override fun unbind() = with(binding) {
        clickContainer.setOnTouchListener(null)
        cancelButton.setOnTouchListener(null)
        visibleButton.setOnTouchListener(null)

        clickContainer.setOnClickListener(null)
        cancelButton.setOnClickListener(null)
        visibleButton.setOnClickListener(null)
    }

    companion object {
        const val MAX_COUNT = 99
        const val MAX_COUNT_TEXT = ":D"

        /** Variable only for UI tests. */
        @RunNone var isMaxTest = false
    }
}