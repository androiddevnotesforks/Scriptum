package sgtmelon.scriptum.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.NotificationHolder
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.inflateBinding
import sgtmelon.scriptum.screen.view.notification.NotificationActivity

/**
 * Адаптер списка уведомлений для [NotificationActivity]
 *
 * @author SerjantArbuz
 */
class NotificationAdapter(@Theme private val theme: Int,
                          private val clickListener: ItemListener.Click
) : ParentAdapter<NoteModel, NotificationHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            NotificationHolder(parent.inflateBinding(R.layout.item_notification), clickListener)

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) =
            holder.bind(theme, list[position])

}