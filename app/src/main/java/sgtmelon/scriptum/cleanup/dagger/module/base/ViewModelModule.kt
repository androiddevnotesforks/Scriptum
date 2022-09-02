package sgtmelon.scriptum.cleanup.dagger.module.base

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Named
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.other.ViewModelFactory
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IBackupPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop.IDevelopInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop.IPrintDevelopInteractor
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControlImpl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.BackupPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop.DevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop.PrintDevelopActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop.ServiceDevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IIntroViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.ISplashViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IBinViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IMainViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IRankViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.IRollNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.INotificationViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IBackupPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.INotePreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IDevelopViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IPrintDevelopViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IServiceDevelopViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.IntroViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.SplashViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.BinViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.MainViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.NotesViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.RankViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.NoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.RollNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.TextNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.notification.AlarmViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.notification.NotificationViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.AlarmPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.BackupPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.NotePreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.PreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop.DevelopViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop.PrintDevelopViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop.ServiceDevelopViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.database.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.database.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter

/**
 * Module for provide viewModel's.
 */
@Module
class ViewModelModule {

    @Provides
    @ActivityScope
    fun provideSplashViewModel(
        activity: SplashActivity,
        preferencesRepo: PreferencesRepo
    ): ISplashViewModel {
        val factory = ViewModelFactory.Splash(activity, preferencesRepo)
        return ViewModelProvider(activity, factory)[SplashViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideIntroViewModel(
        activity: IntroActivity,
        preferencesRepo: PreferencesRepo
    ): IIntroViewModel {
        val factory = ViewModelFactory.Intro(activity, preferencesRepo)
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
        preferencesRepo: PreferencesRepo,
        interactor: INotesInteractor,
        deleteNote: DeleteNoteUseCase
    ): INotesViewModel {
        val factory = ViewModelFactory.MainScreen.Notes(
            fragment, preferencesRepo, interactor, deleteNote
        )
        return ViewModelProvider(fragment, factory)[NotesViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideBinViewModel(
        fragment: BinFragment,
        interactor: IBinInteractor,
        restoreNote: RestoreNoteUseCase
    ): IBinViewModel {
        val factory = ViewModelFactory.MainScreen.Bin(fragment, interactor, restoreNote)
        return ViewModelProvider(fragment, factory)[BinViewModel::class.java]
    }

    //endregion

    //region Note

    @Provides
    @ActivityScope
    fun provideNoteViewModel(
        activity: NoteActivity,
        typeConverter: NoteTypeConverter,
        colorConverter: ColorConverter,
        preferencesRepo: PreferencesRepo
    ): INoteViewModel {
        val factory = ViewModelFactory.NoteScreen.Note(
            activity, typeConverter, colorConverter, preferencesRepo
        )
        return ViewModelProvider(activity, factory)[NoteViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideTextNoteViewModel(
        fragment: TextNoteFragment,
        colorConverter: ColorConverter,
        preferencesRepo: PreferencesRepo,
        interactor: ITextNoteInteractor,
        deleteNote: DeleteNoteUseCase,
        restoreNote: RestoreNoteUseCase
    ): ITextNoteViewModel {
        val factory = ViewModelFactory.NoteScreen.TextNote(
            fragment, colorConverter, preferencesRepo, interactor, deleteNote, restoreNote
        )
        val viewModel = ViewModelProvider(fragment, factory)[TextNoteViewModel::class.java]
        val saveControl = SaveControlImpl(fragment.resources, preferencesRepo.saveState, viewModel)
        viewModel.setSaveControl(saveControl)

        return viewModel
    }

    @Provides
    @ActivityScope
    fun provideRollNoteViewModel(
        fragment: RollNoteFragment,
        colorConverter: ColorConverter,
        preferencesRepo: PreferencesRepo,
        interactor: IRollNoteInteractor,
        deleteNote: DeleteNoteUseCase,
        restoreNote: RestoreNoteUseCase
    ): IRollNoteViewModel {
        val factory = ViewModelFactory.NoteScreen.RollNote(
            fragment, colorConverter, preferencesRepo, interactor, deleteNote, restoreNote
        )
        val viewModel = ViewModelProvider(fragment, factory)[RollNoteViewModel::class.java]
        val saveControl = SaveControlImpl(fragment.resources, preferencesRepo.saveState, viewModel)
        viewModel.setSaveControl(saveControl)

        return viewModel
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideAlarmViewModel(
        activity: AlarmActivity,
        preferencesRepo: PreferencesRepo,
        interactor: IAlarmInteractor,
        getMelodyList: GetMelodyListUseCase
    ): IAlarmViewModel {
        val factory = ViewModelFactory.Alarm(
            activity, preferencesRepo, interactor, getMelodyList
        )

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
        preferencesRepo: PreferencesRepo,
        @Named("Theme") getSummary: GetSummaryUseCase
    ): IPreferenceViewModel {
        val factory = ViewModelFactory.Preference.Main(fragment, preferencesRepo, getSummary)
        return ViewModelProvider(fragment, factory)[PreferenceViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideBackupPreferenceViewModel(
        fragment: BackupPreferenceFragment,
        interactor: IBackupPreferenceInteractor
    ): IBackupPreferenceViewModel {
        val factory = ViewModelFactory.Preference.Backup(fragment, interactor)
        return ViewModelProvider(fragment, factory)[BackupPreferenceViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideNotePreferenceViewModel(
        fragment: NotePreferenceFragment,
        preferencesRepo: PreferencesRepo,
        @Named("Sort") getSortSummary: GetSummaryUseCase,
        @Named("DefaultColor") getDefaultColorSummary: GetSummaryUseCase,
        @Named("SavePeriod") getSavePeriodSummary: GetSummaryUseCase
    ): INotePreferenceViewModel {
        val factory = ViewModelFactory.Preference.Note(
            fragment, preferencesRepo,
            getSortSummary, getDefaultColorSummary, getSavePeriodSummary
        )

        return ViewModelProvider(fragment, factory)[NotePreferenceViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideAlarmPreferenceViewModel(
        fragment: AlarmPreferenceFragment,
        preferencesRepo: PreferencesRepo,
        @Named("Repeat") getRepeatSummary: GetSummaryUseCase,
        @Named("Volume") getVolumeSummary: GetSummaryUseCase,
        getSignalSummary: GetSignalSummaryUseCase,
        getMelodyList: GetMelodyListUseCase
    ): IAlarmPreferenceViewModel {
        val factory = ViewModelFactory.Preference.Alarm(
            fragment,
            preferencesRepo, getRepeatSummary, getVolumeSummary, getSignalSummary,
            getMelodyList
        )
        return ViewModelProvider(fragment, factory)[AlarmPreferenceViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideDevelopViewModel(
        fragment: DevelopFragment,
        interactor: IDevelopInteractor
    ): IDevelopViewModel {
        val factory = ViewModelFactory.Develop.Main(fragment, interactor)
        return ViewModelProvider(fragment, factory)[DevelopViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun providePrintDevelopViewModel(
        activity: PrintDevelopActivity,
        interactor: IPrintDevelopInteractor
    ): IPrintDevelopViewModel {
        val factory = ViewModelFactory.Develop.Print(activity, interactor)
        return ViewModelProvider(activity, factory)[PrintDevelopViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideServiceDevelopViewModel(fragment: ServiceDevelopFragment): IServiceDevelopViewModel {
        val factory = ViewModelFactory.Develop.Service(fragment)
        return ViewModelProvider(fragment, factory)[ServiceDevelopViewModel::class.java]
    }

    //endregion

}