package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.interactor.note.RollNoteInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.repository.room.callback.IRankRepo
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.callback.note.IRollNoteViewModel
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel

@Module
class RollNoteModule {

    @Provides
    @ActivityScope
    fun provideInteractor(iPreferenceRepo: IPreferenceRepo, iAlarmRepo: IAlarmRepo,
                          iRankRepo: IRankRepo, iNoteRepo: INoteRepo,
                          fragment: RollNoteFragment): IRollNoteInteractor {
        return RollNoteInteractor(iPreferenceRepo, iAlarmRepo, iRankRepo, iNoteRepo, fragment)
    }

    @Provides
    @ActivityScope
    fun provideViewModel(fragment: RollNoteFragment, iInteractor: IRollNoteInteractor,
                         iBindInteractor: IBindInteractor): IRollNoteViewModel {
        return ViewModelProvider(fragment).get(RollNoteViewModel::class.java).apply {
            setCallback(fragment)
            setParentCallback(fragment.context as? INoteChild)
            setInteractor(iInteractor, iBindInteractor)
        }
    }

}