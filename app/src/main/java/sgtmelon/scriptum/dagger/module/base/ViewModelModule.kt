package sgtmelon.scriptum.dagger.module.base

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.interactor.callback.main.IMainInteractor
import sgtmelon.scriptum.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.screen.ui.DevelopActivity
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.screen.ui.preference.PreferenceFragment
import sgtmelon.scriptum.screen.vm.DevelopViewModel
import sgtmelon.scriptum.screen.vm.IntroViewModel
import sgtmelon.scriptum.screen.vm.PreferenceViewModel
import sgtmelon.scriptum.screen.vm.SplashViewModel
import sgtmelon.scriptum.screen.vm.callback.IDevelopViewModel
import sgtmelon.scriptum.screen.vm.callback.IIntroViewModel
import sgtmelon.scriptum.screen.vm.callback.IPreferenceViewModel
import sgtmelon.scriptum.screen.vm.callback.ISplashViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IBinViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IMainViewModel
import sgtmelon.scriptum.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IRankViewModel
import sgtmelon.scriptum.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.screen.vm.callback.note.IRollNoteViewModel
import sgtmelon.scriptum.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.INotificationViewModel
import sgtmelon.scriptum.screen.vm.main.BinViewModel
import sgtmelon.scriptum.screen.vm.main.MainViewModel
import sgtmelon.scriptum.screen.vm.main.NotesViewModel
import sgtmelon.scriptum.screen.vm.main.RankViewModel
import sgtmelon.scriptum.screen.vm.note.NoteViewModel
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import sgtmelon.scriptum.screen.vm.notification.NotificationViewModel

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
            setParentCallback(fragment.context as? INoteChild)
            setInteractor(interactor, bindInteractor)
        }
    }


    @Provides
    @ActivityScope
    fun provideRollNoteViewModel(fragment: RollNoteFragment, interactor: IRollNoteInteractor,
                                 bindInteractor: IBindInteractor): IRollNoteViewModel {
        return ViewModelProvider(fragment).get(RollNoteViewModel::class.java).apply {
            setCallback(fragment)
            setParentCallback(fragment.context as? INoteChild)
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
    fun providePreferenceViewModel(fragment: PreferenceFragment, context: Context,
                                   signalInteractor: ISignalInteractor): IPreferenceViewModel {
        return PreferenceViewModel(context, signalInteractor, fragment)
    }

    @Provides
    @ActivityScope
    fun provideDevelopViewModel(activity: DevelopActivity): IDevelopViewModel {
        return ViewModelProvider(activity).get(DevelopViewModel::class.java).apply {
            setCallback(activity)
        }
    }

}