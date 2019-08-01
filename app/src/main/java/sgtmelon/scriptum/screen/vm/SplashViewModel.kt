package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.ui.SplashActivity.Companion.OpenFrom
import sgtmelon.scriptum.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.callback.ISplashViewModel

/**
 * ViewModel for [SplashActivity]
 *
 * @author SerjantArbuz
 */
class SplashViewModel(application: Application) : ParentViewModel<ISplashActivity>(application),
        ISplashViewModel {

    override fun onSetup(bundle: Bundle?) {
        if (bundle == null) {
            onSimpleStart()
        } else {
            when (bundle.getString(SplashActivity.OPEN_SCREEN) ?: "") {
                OpenFrom.BIND -> {
                    val intent = context.getNoteIntent(
                            NoteType.values()[bundle.getInt(NoteData.Intent.TYPE)],
                            bundle.getLong(NoteData.Intent.ID)
                    )

                    callback?.startActivities(arrayOf(MainActivity.getInstance(context), intent))
                }
                OpenFrom.ALARM -> {
                    callback?.startActivity(AlarmActivity.getInstance(context,
                            bundle.getLong(NoteData.Intent.ID),
                            bundle.getInt(NoteData.Intent.COLOR)
                    ))
                }
                else -> onSimpleStart()
            }
        }
    }

    private fun onSimpleStart(firstStart: Boolean = iPreferenceRepo.firstStart) =
            callback?.startActivity(if (firstStart) {
                IntroActivity.getInstance(context)
            } else {
                MainActivity.getInstance(context)
            })

}