package sgtmelon.scriptum.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.NotificationHolder
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity

/**
 * Адаптер списка уведомлений для [NotificationActivity]
 *
 * @author SerjantArbuz
 */
class NotificationAdapter(private val clickListener: ItemListener.Click) :
        ParentAdapter<NotificationItem, NotificationHolder>() {

    @Theme var theme: Int = Theme.UNDEFINED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            NotificationHolder(parent.inflateBinding(R.layout.item_notification), clickListener)

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) =
            holder.bind(theme, list[position])

}