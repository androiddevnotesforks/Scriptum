package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.interactor.main.NotesInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.repository.room.callback.IRankRepo
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

@Module
class NotesModule {

    @Provides
    @ActivityScope
    fun provideInteractor(fragment: NotesFragment, preferenceRepo: IPreferenceRepo,
                          noteRepo: INoteRepo, alarmRepo: IAlarmRepo,
                          rankRepo: IRankRepo): INotesInteractor {
        return NotesInteractor(preferenceRepo, noteRepo, alarmRepo, rankRepo, fragment)
    }

    @Provides
    @ActivityScope
    fun provideViewModel(fragment: NotesFragment, interactor: INotesInteractor,
                         bindInteractor: IBindInteractor): INotesViewModel {
        return ViewModelProvider(fragment).get(NotesViewModel::class.java).apply {
            setCallback(fragment)
            setInteractor(interactor, bindInteractor)
        }
    }

}