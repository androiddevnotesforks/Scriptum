package sgtmelon.scriptum.infrastructure.adapter.holder

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.bindPastTime
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveRank

class NoteTextHolder(
    private val binding: ItemNoteTextBinding
) : NoteParentHolder<NoteItem.Text>(binding.root) {

    override val parentCard: CardView get() = binding.parentCard
    override val clickContainer: ViewGroup get() = binding.clickContainer
    override val nameText: TextView get() = binding.nameText
    override val colorView: View get() = binding.colorView

    override fun bindContent(item: NoteItem.Text) {
        binding.contentText.text = item.text
    }

    override fun bindInfo(item: NoteItem.Text) = with(binding.info) {
        val haveIndicators = with(item) { haveAlarm || isStatus || haveRank }
        indicatorContainer.makeVisibleIf(haveIndicators)
        alarmImage.makeVisibleIf(item.haveAlarm)
        bindImage.makeVisibleIf(item.isStatus)
        rankImage.makeVisibleIf(item.haveRank)

        changeText.bindPastTime(item.change)
        createText.bindPastTime(item.create)
    }
}