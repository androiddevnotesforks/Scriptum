package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.presentation.adapter.diff.NotificationDiff
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.NotificationHolder
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.infrastructure.utils.inflateBinding

/**
 * Adapter which displays list of notifications for [NotificationActivity]
 */
class NotificationAdapter(
    private val clickListener: ItemListener.Click
) : ParentDiffAdapter<NotificationItem, NotificationDiff, NotificationHolder>() {

    override val diff = NotificationDiff()

    override fun setList(list: List<NotificationItem>) = apply {
        super.setList(list)
        this.list.clearAdd(ArrayList(list.map { it.copy() }))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(parent.inflateBinding(R.layout.item_notification), clickListener)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.bind(list.getOrNull(position) ?: return)
    }
}