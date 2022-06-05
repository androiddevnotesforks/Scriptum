package sgtmelon.scriptum.dagger.other

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sgtmelon.scriptum.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.domain.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.IntroViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.SplashViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.main.MainViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.main.RankViewModel
import kotlin.reflect.KClass

/**
 * ViewModel factory for create ViewModels with constructor parameters.
 */
@Suppress("UNCHECKED_CAST")
object ViewModelFactory {

    // region Help func

    private fun onNotFound() = IllegalArgumentException("ViewModel Not Found")

    private fun <T> Class<T>.create(createFunc: () -> Any): T {
        return if (isAssignableFrom(createFunc::class.java)) createFunc() as T else throw onNotFound()
    }

    // TODO check if upper func work - remove it
    private fun <T> Class<T>.create(modelClass: KClass<*>, createFunc: () -> Any): T {
        return if (isAssignableFrom(modelClass.java)) createFunc() as T else throw onNotFound()
    }

    //endregion

    class Splash(
        private val activity: SplashActivity,
        private val interactor: ISplashInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create { SplashViewModel(activity, interactor) }
        }
    }

    class Intro(
        private val activity: IntroActivity,
        private val interactor: IIntroInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create { IntroViewModel(activity, interactor) }
        }
    }

    class Main(
        private val activity: MainActivity
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create { MainViewModel(activity) }
        }
    }

    class Rank(
        private val fragment: RankFragment,
        private val interactor: IRankInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create { RankViewModel(fragment, interactor) }
        }
    }
}