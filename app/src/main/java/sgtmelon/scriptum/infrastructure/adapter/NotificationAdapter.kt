package sgtmelon.scriptum.infrastructure.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NotificationClickListener
import sgtmelon.scriptum.infrastructure.adapter.diff.manual.NotificationDiff
import sgtmelon.scriptum.infrastructure.adapter.holder.NotificationHolder
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentManualAdapter
import sgtmelon.scriptum.infrastructure.utils.extensions.inflateBinding

/**
 * Adapter which displays list of [NotificationItem]'s.
 */
class NotificationAdapter(
    private val callback: NotificationClickListener
) : ParentManualAdapter<NotificationItem, NotificationHolder>(NotificationDiff()) {

    override fun getListCopy(list: List<NotificationItem>): List<NotificationItem> {
        return ArrayList(list.map { it.copy() })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(parent.inflateBinding(R.layout.item_notification))
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.bind(getItem(position), callback)
    }
}