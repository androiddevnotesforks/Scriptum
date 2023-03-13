package sgtmelon.scriptum.infrastructure.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem

/**
 * Diff for [RankItem].
 */
class RankDiff : DiffUtil.ItemCallback<RankItem>() {

    override fun areItemsTheSame(oldItem: RankItem, newItem: RankItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RankItem, newItem: RankItem): Boolean {
        return oldItem == newItem
    }
}