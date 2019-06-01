package sgtmelon.scriptum.screen.callback.notification

import android.content.Intent
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.view.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.notification.NotificationViewModel

/**
 * Интерфейс для общения [NotificationViewModel] с [NotificationActivity]
 *
 * @author SerjantArbuz
 */
interface NotificationCallback {

    fun setupToolbar()

    fun setupRecycler(@Theme theme: Int)

    fun notifyDataSetChanged(list: MutableList<NoteModel>)

    fun notifyItemRemoved(p: Int, list: MutableList<NoteModel>)

    fun startActivity(intent: Intent)

}