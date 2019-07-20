package sgtmelon.scriptum.screen.view.callback.notification

import android.content.Intent
import androidx.annotation.ColorInt
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.view.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

/**
 * Интерфейс для общения [AlarmViewModel] с [AlarmActivity]
 *
 * @author SerjantArbuz
 */
interface IAlarmActivity {

    fun setupView(@Theme theme: Int)

    fun notifyDataSetChanged(noteModel: NoteModel)

    fun startRippleAnimation(@Theme theme: Int, @ColorInt fillColor: Int)

    fun startControlFadeAnimation()

    fun startActivity(intent: Intent)

    fun finish()

}