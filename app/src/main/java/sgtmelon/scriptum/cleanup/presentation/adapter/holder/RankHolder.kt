package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.databinding.ItemRankBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.ItemDragListener
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.callback.click.RankClickListener
import sgtmelon.scriptum.infrastructure.utils.makeVisibleIf
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

        val touchListener = View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                dragListener.setDrag(v.id == clickContainer.id)
            }

            return@OnTouchListener false
        }

        clickContainer.setOnTouchListener(touchListener)
        cancelButton.setOnTouchListener(touchListener)
        visibleButton.setOnTouchListener(touchListener)

        clickContainer.setOnClickListener { checkPosition { callback.onRankClick(it) } }
        cancelButton.setOnClickListener { checkPosition { callback.onRankCancelClick(it) } }
        visibleButton.setOnClickListener { checkPosition { onVisibleClick(callback, item, it) } }

        bindContent(item, withAnim = false)
    }

    private fun bindContent(item: RankItem, withAnim: Boolean) {
        val visibleDescription = if (item.isVisible) {
            context.getString(R.string.desc_rank_hide, item.name)
        } else {
            context.getString(R.string.desc_rank_show, item.name)
        }
        binding.visibleButton.contentDescription = visibleDescription
        binding.visibleButton.setDrawable(item.isVisible, withAnim)

        binding.nameText.text = item.name

        notifyIndicators(item)

        val cancelDescription = context.getString(R.string.desc_rank_cancel, item.name)
        binding.cancelButton.contentDescription = cancelDescription
    }

    private fun notifyIndicators(item: RankItem) {
        val isAlarmVisible = isMaxTest || item.notificationCount != 0
        val isBindVisible = isMaxTest || item.bindCount != 0

        binding.imageContainer.makeVisibleIf(condition = isAlarmVisible || isBindVisible)

        binding.notificationContainer.makeVisibleIf(isAlarmVisible)
        binding.notificationText.text = getIndicatorCount(item.notificationCount)

        binding.bindContainer.makeVisibleIf(isBindVisible)
        binding.bindText.text = getIndicatorCount(item.bindCount)

        binding.countText.text = context.getString(R.string.list_item_rank_count, item.noteId.size)
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

    override fun unbind() {
        binding.clickContainer.setOnTouchListener(null)
        binding.cancelButton.setOnTouchListener(null)
        binding.visibleButton.setOnTouchListener(null)

        binding.clickContainer.setOnClickListener(null)
        binding.cancelButton.setOnClickListener(null)
        binding.visibleButton.setOnClickListener(null)
    }

    companion object {
        const val MAX_COUNT = 99
        const val MAX_COUNT_TEXT = ":D"

        /** Variable only for UI tests. */
        @RunNone var isMaxTest = false
    }
}