package sgtmelon.scriptum.screen.vm

import android.app.Application
import sgtmelon.scriptum.screen.callback.NotificationCallback
import sgtmelon.scriptum.screen.view.NotificationActivity

/**
 * ViewModel для [NotificationActivity]
 *
 * @author SerjantArbuz
 */
class NotificationViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: NotificationCallback

}