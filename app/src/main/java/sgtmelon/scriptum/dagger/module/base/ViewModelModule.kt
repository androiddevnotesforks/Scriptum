package sgtmelon.scriptum.dagger.module.base

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.domain.interactor.callback.*
import sgtmelon.scriptum.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IMainInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.ui.impl.DevelopActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IDevelopViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.IIntroViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.IPreferenceViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.ISplashViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IBinViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IMainViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IRankViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.note.IRollNoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.INotificationViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.DevelopViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.IntroViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.PreferenceViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.SplashViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.main.BinViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.main.MainViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.main.NotesViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.main.RankViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.note.NoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.note.RollNoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.note.TextNoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.notification.AlarmViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.notification.NotificationViewModel

/**
 * Module for provide viewModel's.
 */
@Module
class ViewModelModule {

    @Provides
    @ActivityScope
    fun provideSplashViewModel(activity: SplashActivity,
                               interactor: ISplashInteractor): ISplashViewModel {
        return ViewModelProvider(activity).get(SplashViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }

    @Provides
    @ActivityScope
    fun provideIntroViewModel(activity: IntroActivity,
                              interactor: IIntroInteractor): IIntroViewModel {
        return ViewModelProvider(activity).get(IntroViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }

    //region Main

    @Provides
    @ActivityScope
    fun provideMainViewModel(activity: MainActivity, interactor: IMainInteractor,
                             bindInteractor: IBindInteractor): IMainViewModel {
        return ViewModelProvider(activity).get(MainViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor, bindInteractor)
        }
    }

    @Provides
    @ActivityScope
    fun provideRankViewModel(fragment: RankFragment, interactor: IRankInteractor,
                             bindInteractor: IBindInteractor): IRankViewModel {
        return ViewModelProvider(fragment).get(RankViewModel::class.java).apply {
            setCallback(fragment)
            setInteractor(interactor, bindInteractor)
        }
    }

    @Provides
    @ActivityScope
    fun provideNotesViewModel(fragment: NotesFragment, interactor: INotesInteractor,
                         bindInteractor: IBindInteractor): INotesViewModel {
        return ViewModelProvider(fragment).get(NotesViewModel::class.java).apply {
            setCallback(fragment)
            setInteractor(interactor, bindInteractor)
        }
    }

    @Provides
    @ActivityScope
    fun provideBinViewModel(fragment: BinFragment, interactor: IBinInteractor): IBinViewModel {
        return ViewModelProvider(fragment).get(BinViewModel::class.java).apply {
            setCallback(fragment)
            setInteractor(interactor)
        }
    }


    //endregion

    //region Note

    @Provides
    @ActivityScope
    fun provideNoteViewModel(activity: NoteActivity, interactor: INoteInteractor): INoteViewModel {
        return ViewModelProvider(activity).get(NoteViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }

    @Provides
    @ActivityScope
    fun provideTextNoteViewModel(fragment: TextNoteFragment, interactor: ITextNoteInteractor,
                                 bindInteractor: IBindInteractor): ITextNoteViewModel {
        return ViewModelProvider(fragment).get(TextNoteViewModel::class.java).apply {
            setCallback(fragment)
            setParentCallback(fragment.context as? INoteConnector)
            setInteractor(interactor, bindInteractor)
        }
    }


    @Provides
    @ActivityScope
    fun provideRollNoteViewModel(fragment: RollNoteFragment, interactor: IRollNoteInteractor,
                                 bindInteractor: IBindInteractor): IRollNoteViewModel {
        return ViewModelProvider(fragment).get(RollNoteViewModel::class.java).apply {
            setCallback(fragment)
            setParentCallback(fragment.context as? INoteConnector)
            setInteractor(interactor, bindInteractor)
        }
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideAlarmViewModel(activity: AlarmActivity, interactor: IAlarmInteractor,
                              signalInteractor: ISignalInteractor,
                              bindInteractor: IBindInteractor): IAlarmViewModel {
        return ViewModelProvider(activity).get(AlarmViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor, signalInteractor, bindInteractor)
        }
    }

    @Provides
    @ActivityScope
    fun provideNotificationViewModel(activity: NotificationActivity,
                                     interactor: INotificationInteractor): INotificationViewModel {
        return ViewModelProvider(activity).get(NotificationViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }


    @Provides
    @ActivityScope
    fun providePreferenceViewModel(fragment: PreferenceFragment,
                                   preferenceInteractor: IPreferenceInteractor,
                                   signalInteractor: ISignalInteractor,
                                   backupInteractor: IBackupInteractor): IPreferenceViewModel {
        return PreferenceViewModel(
                preferenceInteractor, signalInteractor, backupInteractor, fragment
        )
    }

    @Provides
    @ActivityScope
    fun provideDevelopViewModel(activity: DevelopActivity,
                                interactor: IDevelopInteractor): IDevelopViewModel {
        return ViewModelProvider(activity).get(DevelopViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }

}