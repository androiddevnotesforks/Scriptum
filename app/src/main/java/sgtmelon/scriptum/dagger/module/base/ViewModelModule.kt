package sgtmelon.scriptum.dagger.module.base

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.dagger.other.ViewModelFactory
import sgtmelon.scriptum.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.domain.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.interactor.callback.preference.IAlarmPreferenceInteractor
import sgtmelon.scriptum.domain.interactor.callback.preference.IBackupPreferenceInteractor
import sgtmelon.scriptum.domain.interactor.callback.preference.INotePreferenceInteractor
import sgtmelon.scriptum.domain.interactor.callback.preference.IPreferenceInteractor
import sgtmelon.scriptum.domain.interactor.callback.preference.develop.IDevelopInteractor
import sgtmelon.scriptum.domain.interactor.callback.preference.develop.IPrintDevelopInteractor
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
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
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.BackupPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.DevelopFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.PrintDevelopActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop.ServiceDevelopFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.IIntroViewModel
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
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IBackupPreferenceViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.INotePreferenceViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPreferenceViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IDevelopViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IPrintDevelopViewModel
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IServiceDevelopViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.IntroViewModel
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
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.AlarmPreferenceViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.BackupPreferenceViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.NotePreferenceViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.PreferenceViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.develop.DevelopViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.develop.PrintDevelopViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.preference.develop.ServiceDevelopViewModel

/**
 * Module for provide viewModel's.
 */
@Module
class ViewModelModule {

    @Provides
    @ActivityScope
    fun provideSplashViewModel(
        activity: SplashActivity,
        interactor: ISplashInteractor
    ): ISplashViewModel {
        val factory = ViewModelFactory.Splash(activity, interactor)
        return ViewModelProvider(activity, factory)[SplashViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideIntroViewModel(
        activity: IntroActivity,
        interactor: IIntroInteractor
    ): IIntroViewModel {
        val factory = ViewModelFactory.Intro(activity, interactor)
        return ViewModelProvider(activity, factory)[IntroViewModel::class.java]
    }

    //region Main

    @Provides
    @ActivityScope
    fun provideMainViewModel(activity: MainActivity): IMainViewModel {
        val factory = ViewModelFactory.MainScreen.Main(activity)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideRankViewModel(
        fragment: RankFragment,
        interactor: IRankInteractor
    ): IRankViewModel {
        val factory = ViewModelFactory.MainScreen.Rank(fragment, interactor)
        return ViewModelProvider(fragment, factory)[RankViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideNotesViewModel(
        fragment: NotesFragment,
        interactor: INotesInteractor
    ): INotesViewModel {
        val factory = ViewModelFactory.MainScreen.Notes(fragment, interactor)
        return ViewModelProvider(fragment, factory)[NotesViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideBinViewModel(fragment: BinFragment, interactor: IBinInteractor): IBinViewModel {
        val factory = ViewModelFactory.MainScreen.Bin(fragment, interactor)
        return ViewModelProvider(fragment, factory)[BinViewModel::class.java]
    }

    //endregion

    //region Note

    @Provides
    @ActivityScope
    fun provideNoteViewModel(activity: NoteActivity, interactor: INoteInteractor): INoteViewModel {
        val factory = ViewModelFactory.NoteScreen.Note(activity, interactor)
        return ViewModelProvider(activity, factory)[NoteViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideTextNoteViewModel(
        fragment: TextNoteFragment,
        interactor: ITextNoteInteractor
    ): ITextNoteViewModel {
        val factory = ViewModelFactory.NoteScreen.TextNote(fragment, interactor)
        val viewModel = ViewModelProvider(fragment, factory)[TextNoteViewModel::class.java]
        val saveControl = SaveControl(fragment.resources, interactor.getSaveModel(), viewModel)
        viewModel.setSaveControl(saveControl)

        return viewModel
    }

    @Provides
    @ActivityScope
    fun provideRollNoteViewModel(
        fragment: RollNoteFragment,
        interactor: IRollNoteInteractor
    ): IRollNoteViewModel {
        val factory = ViewModelFactory.NoteScreen.RollNote(fragment, interactor)
        val viewModel = ViewModelProvider(fragment, factory)[RollNoteViewModel::class.java]
        val saveControl = SaveControl(fragment.resources, interactor.getSaveModel(), viewModel)
        viewModel.setSaveControl(saveControl)

        return viewModel
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideAlarmViewModel(
        activity: AlarmActivity,
        interactor: IAlarmInteractor,
        signalInteractor: ISignalInteractor
    ): IAlarmViewModel {
        val factory = ViewModelFactory.Alarm(activity, interactor, signalInteractor)
        return ViewModelProvider(activity, factory)[AlarmViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideNotificationViewModel(
        activity: NotificationActivity,
        interactor: INotificationInteractor
    ): INotificationViewModel {
        val factory = ViewModelFactory.Notification(activity, interactor)
        return ViewModelProvider(activity, factory)[NotificationViewModel::class.java]
    }

    //region Preference

    @Provides
    @ActivityScope
    fun providePreferenceViewModel(
        fragment: PreferenceFragment,
        interactor: IPreferenceInteractor
    ): IPreferenceViewModel {
        return ViewModelProvider(fragment).get(PreferenceViewModel::class.java).apply {
            setCallback(fragment)
            setInteractor(interactor)
        }
    }

    @Provides
    @ActivityScope
    fun provideBackupPreferenceViewModel(
        fragment: BackupPreferenceFragment,
        interactor: IBackupPreferenceInteractor
    ): IBackupPreferenceViewModel {
        return ViewModelProvider(fragment).get(BackupPreferenceViewModel::class.java).apply {
            setCallback(fragment)
            setInteractor(interactor)
        }
    }

    @Provides
    @ActivityScope
    fun provideNotePreferenceViewModel(
        fragment: NotePreferenceFragment,
        interactor: INotePreferenceInteractor
    ): INotePreferenceViewModel {
        return ViewModelProvider(fragment).get(NotePreferenceViewModel::class.java).apply {
            setCallback(fragment)
            setInteractor(interactor)
        }
    }

    @Provides
    @ActivityScope
    fun provideAlarmPreferenceViewModel(
        fragment: AlarmPreferenceFragment,
        interactor: IAlarmPreferenceInteractor,
        signalInteractor: ISignalInteractor
    ): IAlarmPreferenceViewModel {
        return ViewModelProvider(fragment).get(AlarmPreferenceViewModel::class.java).apply {
            setCallback(fragment)
            setInteractor(interactor, signalInteractor)
        }
    }

    @Provides
    @ActivityScope
    fun provideDevelopViewModel(
        fragment: DevelopFragment,
        interactor: IDevelopInteractor
    ): IDevelopViewModel {
        return ViewModelProvider(fragment).get(DevelopViewModel::class.java).apply {
            setCallback(fragment)
            setInteractor(interactor)
        }
    }

    @Provides
    @ActivityScope
    fun providePrintDevelopViewModel(
        activity: PrintDevelopActivity,
        interactor: IPrintDevelopInteractor
    ): IPrintDevelopViewModel {
        return ViewModelProvider(activity).get(PrintDevelopViewModel::class.java).apply {
            setCallback(activity)
            setInteractor(interactor)
        }
    }

    @Provides
    @ActivityScope
    fun provideServiceDevelopViewModel(fragment: ServiceDevelopFragment): IServiceDevelopViewModel {
        return ViewModelProvider(fragment).get(ServiceDevelopViewModel::class.java).apply {
            setCallback(fragment)
        }
    }

    //endregion

}