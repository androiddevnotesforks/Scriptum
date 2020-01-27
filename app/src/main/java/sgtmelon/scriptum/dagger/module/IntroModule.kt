package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.IntroInteractor
import sgtmelon.scriptum.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.vm.IntroViewModel
import sgtmelon.scriptum.screen.vm.callback.IIntroViewModel

@Module
class IntroModule {

    @Provides
    @ActivityScope
    fun provideInteractor(preferenceRepo: IPreferenceRepo): IIntroInteractor {
        return IntroInteractor(preferenceRepo)
    }

    @Provides
    @ActivityScope
    fun provideViewModel(activity: IntroActivity, interactor: IIntroInteractor): IIntroViewModel {
        return ViewModelProvider(activity).get(IntroViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }

}