package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.IAppInteractor
import sgtmelon.scriptum.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.interactor.note.NoteInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.vm.AppViewModel
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.screen.vm.note.NoteViewModel
import javax.inject.Named

@Module
class NoteModule {

    @Provides
    @ActivityScope
    fun provideAppViewModel(activity: NoteActivity, interactor: IAppInteractor): IAppViewModel {
        return ViewModelProvider(activity).get(AppViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }


    @Provides
    @ActivityScope
    fun provideInteractor(preferenceRepo: IPreferenceRepo): INoteInteractor {
        return NoteInteractor(preferenceRepo)
    }

    @Provides
    @ActivityScope
    fun provideViewModel(activity: NoteActivity, interactor: INoteInteractor): INoteViewModel {
        return ViewModelProvider(activity).get(NoteViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }

}