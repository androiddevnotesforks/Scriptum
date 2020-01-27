package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.interactor.main.BinInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.screen.vm.callback.main.IBinViewModel
import sgtmelon.scriptum.screen.vm.main.BinViewModel

@Module
class BinModule {

    @Provides
    @ActivityScope
    fun provideInteractor(iPreferenceRepo: IPreferenceRepo, iNoteRepo: INoteRepo,
                          fragment: BinFragment): IBinInteractor {
        return BinInteractor(iPreferenceRepo, iNoteRepo, fragment)
    }

    @Provides
    @ActivityScope
    fun provideViewModel(fragment: BinFragment, iInteractor: IBinInteractor): IBinViewModel {
        return ViewModelProvider(fragment).get(BinViewModel::class.java).apply {
            setCallback(fragment)
            setInteractor(iInteractor)
        }
    }


}