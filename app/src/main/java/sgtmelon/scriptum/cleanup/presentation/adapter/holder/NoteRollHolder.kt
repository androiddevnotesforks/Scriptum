package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.bindDrawable
import sgtmelon.scriptum.cleanup.extension.bindPastTime
import sgtmelon.scriptum.databinding.IncItemNoteRollBinding
import sgtmelon.scriptum.databinding.ItemNoteRollBinding
import sgtmelon.scriptum.infrastructure.adapter.holder.NoteParentHolder
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveRank
import sgtmelon.scriptum.infrastructure.utils.extensions.note.visibleList

class NoteRollHolder(
    private val binding: ItemNoteRollBinding
) : NoteParentHolder<NoteItem.Roll>(binding.root) {

    override val parentCard: CardView get() = binding.parentCard
    override val clickContainer: ViewGroup get() = binding.clickContainer
    override val nameText: TextView get() = binding.content.nameText
    override val colorView: View get() = binding.colorView

    private val rowBindingList: List<IncItemNoteRollBinding>
        get() = with(binding.content) { listOf(firstRow, secondRow, thirdRow, fourthRow) }

    override fun bindContent(item: NoteItem.Roll) {
        val list = item.visibleList.takeIf { it.isNotEmpty() } ?: item.list

        for((i, row) in rowBindingList.withIndex()) {
            val roll = list.getOrNull(i)

            row.parentContainer.makeVisibleIf(condition = roll != null)

            /**
             * We must specify visible parentContainer or not for every bind. Don't skip
             * [makeVisible] case.
             */
            if (roll == null) continue

            val icon = if (roll.isCheck) R.drawable.ic_check_done else R.drawable.ic_check_outline
            row.checkImage.bindDrawable(icon)
            row.contentText.text = roll.text
        }
    }

    override fun bindInfo(item: NoteItem.Roll) = with(binding.content.info) {
        progressText.text = item.text

        val haveIndicators = with(item) { !isVisible || haveAlarm || isStatus || haveRank }
        indicatorContainer.makeVisibleIf(haveIndicators)
        visibleImage.makeVisibleIf(!item.isVisible)
        alarmImage.makeVisibleIf(item.haveAlarm)
        bindImage.makeVisibleIf(item.isStatus)
        rankImage.makeVisibleIf(item.haveRank)

        changeText.bindPastTime(item.change)
        createText.bindPastTime(item.create)
    }
}