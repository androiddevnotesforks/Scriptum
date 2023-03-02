package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.bindIndicatorColor
import sgtmelon.scriptum.cleanup.extension.bindNoteColor
import sgtmelon.scriptum.cleanup.extension.bindPastTime
import sgtmelon.scriptum.cleanup.extension.getDisplayedTheme
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NoteClickListener
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.utils.extensions.makeGone
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveRank

class NoteTextHolder(
    private val binding: ItemNoteTextBinding
) : ParentHolder(binding.root),
    UnbindCallback {

    fun bind(item: NoteItem.Text, callback: NoteClickListener) = with(binding) {
        bindTheme(item)

        nameText.text = item.name
        nameText.makeVisibleIf(item.name.isNotEmpty())
        contentText.text = item.text

        bindInfo(item)

        clickContainer.setOnClickListener { callback.onNoteClick(item) }
        clickContainer.setOnLongClickListener {
            checkPosition { callback.onNoteLongClick(item, it) }
            return@setOnLongClickListener true
        }
    }

    /**
     * [ThemeDisplayed.LIGHT] - set color only for card and hide indicator.
     * [ThemeDisplayed.DARK]  - set color for indicator, don't set anything for card
     */
    private fun bindTheme(item: NoteItem.Text) {
        when (context.getDisplayedTheme() ?: return) {
            ThemeDisplayed.LIGHT -> {
                binding.parentCard.bindNoteColor(item.color)
                binding.colorView.makeGone()
            }
            ThemeDisplayed.DARK -> {
                binding.colorView.makeVisible()
                binding.colorView.bindIndicatorColor(item.color)
            }
        }
    }

    private fun bindInfo(item: NoteItem.Text) = with(binding.info) {
        indicatorContainer.makeVisibleIf(condition = item.haveAlarm || item.isStatus || item.haveRank)
        alarmImage.makeVisibleIf(item.haveAlarm)
        bindImage.makeVisibleIf(item.isStatus)
        rankImage.makeVisibleIf(item.haveRank)

        changeText.bindPastTime(item.change)
        createText.bindPastTime(item.create)
    }

    override fun unbind() = with(binding) {
        clickContainer.setOnClickListener(null)
        clickContainer.setOnLongClickListener(null)
    }
}