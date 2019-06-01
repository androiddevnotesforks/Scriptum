package sgtmelon.scriptum.screen.callback.notification

import android.content.Intent
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.view.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

/**
 * Интерфейс для общения [AlarmViewModel] с [AlarmActivity]
 *
 * @author SerjantArbuz
 */
interface AlarmCallback {

    fun setupView(theme: Int)

    fun notifyDataSetChanged(noteModel: NoteModel)

    fun animateControlShow()

    fun startActivity(intent: Intent)

    fun finish()

}