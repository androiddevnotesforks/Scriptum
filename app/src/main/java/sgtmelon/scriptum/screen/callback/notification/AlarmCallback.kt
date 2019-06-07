package sgtmelon.scriptum.screen.callback.notification

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
interface AlarmCallback {

    fun setupView(@Theme theme: Int)

    fun notifyDataSetChanged(noteModel: NoteModel)

    fun animateCircularColor(@Theme theme: Int, @ColorInt fillColor: Int)

    fun animateControlShow()

    fun startActivity(intent: Intent)

    fun finish()

}