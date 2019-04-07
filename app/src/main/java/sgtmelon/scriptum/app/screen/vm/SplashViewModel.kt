package sgtmelon.scriptum.app.screen.vm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.app.model.data.NoteData
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.screen.callback.SplashCallback
import sgtmelon.scriptum.app.screen.view.IntroActivity
import sgtmelon.scriptum.app.screen.view.MainActivity
import sgtmelon.scriptum.app.screen.view.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.app.screen.view.SplashActivity
import sgtmelon.scriptum.office.utils.Preference


/**
 * ViewModel для [SplashActivity]
 */
class SplashViewModel : ViewModel() {

    lateinit var context: Context
    lateinit var callback: SplashCallback

    private val prefUtils by lazy { Preference(context) }

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
            callback.startNormal(Intent(context, when (prefUtils.firstStart) {
                true -> IntroActivity::class.java
                false -> MainActivity::class.java
            }))
        }
    }

}