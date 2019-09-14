package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.interactor.splash.ISplashInteractor
import sgtmelon.scriptum.interactor.splash.SplashInteractor
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.ui.SplashActivity.Companion.OpenFrom
import sgtmelon.scriptum.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.callback.ISplashViewModel

/**
 * ViewModel for [SplashActivity]
 */
class SplashViewModel(application: Application) : ParentViewModel<ISplashActivity>(application),
        ISplashViewModel {

    private val iInteractor: ISplashInteractor = SplashInteractor(context)

    override fun onSetup(bundle: Bundle?) {
        viewModelScope.launch { iInteractor.clearPastAlarm(callback) }

        if (bundle == null) {
            onSimpleStart()
        } else {
            when (bundle.getString(SplashActivity.OPEN_SCREEN) ?: "") {
                OpenFrom.BIND -> {
                    val intent = NoteActivity.getInstance(context,
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

    private fun onSimpleStart(firstStart: Boolean = iInteractor.firstStart) =
            callback?.startActivity(if (firstStart) {
                IntroActivity.getInstance(context)
            } else {
                MainActivity.getInstance(context)
            })

}