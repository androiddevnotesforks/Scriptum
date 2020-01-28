package sgtmelon.scriptum.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.screen.ui.preference.PreferenceFragment
import sgtmelon.scriptum.screen.vm.PreferenceViewModel
import sgtmelon.scriptum.screen.vm.callback.IPreferenceViewModel

@Module
class PreferenceModule {

    @Provides
    @ActivityScope
    fun provideViewModel(fragment: PreferenceFragment, context: Context,
                         signalInteractor: ISignalInteractor): IPreferenceViewModel {
        return PreferenceViewModel(context, signalInteractor, fragment)
    }

}