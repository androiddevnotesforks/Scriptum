package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.interactor.note.TextNoteInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.repository.room.callback.IRankRepo
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel

@Module
class TextNoteModule {

    @Provides
    @ActivityScope
    fun provideInteractor(iPreferenceRepo: IPreferenceRepo, iAlarmRepo: IAlarmRepo,
                          iRankRepo: IRankRepo, iNoteRepo: INoteRepo,
                          fragment: TextNoteFragment): ITextNoteInteractor {
        return TextNoteInteractor(iPreferenceRepo, iAlarmRepo, iRankRepo, iNoteRepo, fragment)
    }

    @Provides
    @ActivityScope
    fun provideViewModel(fragment: TextNoteFragment, iInteractor: ITextNoteInteractor,
                         iBindInteractor: IBindInteractor): ITextNoteViewModel {
        return ViewModelProvider(fragment).get(TextNoteViewModel::class.java).apply {
            setCallback(fragment)
            setParentCallback(fragment.context as? INoteChild)
            setInteractor(iInteractor, iBindInteractor)
        }
    }

}