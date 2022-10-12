package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.presentation.adapter.diff.NotificationDiff
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.NotificationHolder
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.infrastructure.utils.inflateBinding

/**
 * Adapter which displays list of notifications for [NotificationActivity]
 */
class NotificationAdapter(
    private val callback: ItemListener.Click
) : ParentDiffAdapter<NotificationItem, NotificationHolder>(NotificationDiff()) {

    override fun getListCopy(list: List<NotificationItem>): List<NotificationItem> {
        return ArrayList(list.map { it.copy() })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(parent.inflateBinding(R.layout.item_notification))
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list.getOrNull(position) ?: return
        holder.bind(item, callback)
    }
}