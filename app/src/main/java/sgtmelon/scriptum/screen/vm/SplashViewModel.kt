package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.content.Intent
import android.os.Bundle
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.callback.SplashCallback
import sgtmelon.scriptum.screen.view.SplashActivity
import sgtmelon.scriptum.screen.view.intro.IntroActivity
import sgtmelon.scriptum.screen.view.main.MainActivity
import sgtmelon.scriptum.screen.view.note.NoteActivity.Companion.getNoteIntent

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
            val intentTo = if (preference.firstStart) {
                IntroActivity::class.java
            } else {
                MainActivity::class.java
            }

            callback.startNormal(Intent(context, intentTo))
        }
    }

}