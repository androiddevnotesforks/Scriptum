package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.screen.ui.DevelopActivity
import sgtmelon.scriptum.screen.ui.callback.IDevelopActivity
import sgtmelon.scriptum.screen.vm.DevelopViewModel
import sgtmelon.scriptum.screen.vm.callback.IDevelopViewModel

@Module
class DevelopModule {

    @Provides
    @ActivityScope
    fun provideViewModel(activity: DevelopActivity): IDevelopViewModel {
        return ViewModelProvider(activity).get(DevelopViewModel::class.java).apply {
            setCallback(activity)
        }
    }

}