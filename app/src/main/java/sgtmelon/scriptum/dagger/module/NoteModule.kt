package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.interactor.note.NoteInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.screen.vm.note.NoteViewModel

@Module
class NoteModule {

    @Provides
    @ActivityScope
    fun provideInteractor(iPreferenceRepo: IPreferenceRepo): INoteInteractor {
        return NoteInteractor(iPreferenceRepo)
    }

    @Provides
    @ActivityScope
    fun provideViewModel(activity: NoteActivity, iInteractor: INoteInteractor): INoteViewModel {
        return ViewModelProvider(activity).get(NoteViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(iInteractor)
        }
    }


}