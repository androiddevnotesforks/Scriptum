package sgtmelon.scriptum.dagger.other

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sgtmelon.scriptum.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.domain.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.presentation.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity_MembersInjector
import sgtmelon.scriptum.presentation.screen.vm.impl.IntroViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.SplashViewModel

/**
 * ViewModel factory for create ViewModels with constructor parameters.
 */
@Suppress("UNCHECKED_CAST")
object ViewModelFactory {

    fun onNotFound() = IllegalArgumentException("ViewModel Not Found")

    class Splash(
        private val activity: SplashActivity,
        private val interactor: ISplashInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
                SplashViewModel(activity, interactor) as T
            } else {
                throw onNotFound()
            }
        }
    }

    class Intro(
        private val activity: IntroActivity,
        private val interactor: IIntroInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(IntroViewModel::class.java)) {
                IntroViewModel(activity, interactor) as T
            } else {
                throw onNotFound()
            }
        }
    }

}