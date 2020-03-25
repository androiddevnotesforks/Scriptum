package sgtmelon.scriptum.presentation.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.extension.clearAddAll
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.presentation.adapter.diff.NotificationDiff
import sgtmelon.scriptum.presentation.adapter.holder.NotificationHolder
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity

/**
 * Adapter which displays list of notifications for [NotificationActivity]
 */
class NotificationAdapter(private val clickListener: ItemListener.Click) :
        ParentDiffAdapter<NotificationItem, NotificationDiff, NotificationHolder>() {

    @Theme var theme: Int = Theme.UNDEFINED


    override val diff = NotificationDiff()

    override fun setList(list: List<NotificationItem>) = apply {
        super.setList(list)
        this.list.clearAddAll(ArrayList(list.map { it.copy() }))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(parent.inflateBinding(R.layout.item_notification), clickListener)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.bind(theme, list[position])
    }

}