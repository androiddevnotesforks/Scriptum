package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.IAppInteractor
import sgtmelon.scriptum.presentation.screen.ui.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.vm.AppViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.IAppViewModel

/**
 * Module for [NoteActivity].
 */
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

}