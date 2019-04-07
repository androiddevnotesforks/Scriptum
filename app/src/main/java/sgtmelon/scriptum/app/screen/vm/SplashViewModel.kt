package sgtmelon.scriptum.app.screen.vm

import android.app.Application
import android.content.Intent
import android.os.Bundle
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.screen.callback.SplashCallback
import sgtmelon.scriptum.app.screen.view.SplashActivity
import sgtmelon.scriptum.app.screen.view.intro.IntroActivity
import sgtmelon.scriptum.app.screen.view.main.MainActivity
import sgtmelon.scriptum.app.screen.view.note.NoteActivity.Companion.getNoteIntent

/**
 * ViewModel для [SplashActivity]
 *
 * @author SerjantArbuz
 */
class SplashViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: SplashCallback

    fun onStartApplication(bundle: Bundle?) {
        if (bundle != null && bundle.getBoolean(SplashActivity.STATUS_OPEN)) {
            val noteIntent = context.getNoteIntent(
                    NoteType.values()[bundle.getInt(NoteData.Intent.TYPE)],
                    bundle.getLong(NoteData.Intent.ID)
            )

            callback.startFromNotification(
                    arrayOf(Intent(context, MainActivity::class.java), noteIntent)
            )
        } else {
            callback.startNormal(Intent(context, when (preference.firstStart) {
                true -> IntroActivity::class.java
                false -> MainActivity::class.java
            }))
        }
    }

}