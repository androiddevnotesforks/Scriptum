package sgtmelon.scriptum.cleanup.dagger.module

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dagger.Module
import dagger.Provides
import javax.inject.Named
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.other.ViewModelFactory
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.INotificationInteractor
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
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.BackupPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop.PrintDevelopActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IIntroViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.ISplashViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IBinViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IMainViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IRankViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.IRollNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.INotificationViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IAlarmPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IBackupPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IPrintDevelopViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.IntroViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.SplashViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.BinViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.MainViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.NotesViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.RankViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.NoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.RollNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.TextNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.notification.NotificationViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.AlarmPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.BackupPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop.PrintDevelopViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.develop.screen.develop.DevelopViewModel
import sgtmelon.scriptum.develop.screen.develop.DevelopViewModelImpl
import sgtmelon.scriptum.develop.screen.service.ServiceDevelopViewModel
import sgtmelon.scriptum.develop.screen.service.ServiceDevelopViewModelImpl
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.ShiftDateIfExistUseCase
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupExportUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupImportUseCase
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.main.GetNoteListUseCase
import sgtmelon.scriptum.domain.useCase.main.SortNoteListUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateRollCheckUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateRollVisibleUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.domain.useCase.rank.CorrectPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.DeleteRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankListUseCase
import sgtmelon.scriptum.domain.useCase.rank.InsertRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmViewModel
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeViewModel
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeViewModelImpl

@Module
class ViewModelModule {

    @Provides
    @ActivityScope
    fun provideThemeViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo
    ): ThemeViewModel {
        val factory = ViewModelFactory.Theme(preferencesRepo)
        return ViewModelProvider(owner, factory)[ThemeViewModelImpl::class.java]
    }

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
        interactor: IRankInteractor,
        getList: GetRankListUseCase,
        insertRank: InsertRankUseCase,
        deleteRank: DeleteRankUseCase,
        updateRank: UpdateRankUseCase,
        correctPositions: CorrectPositionsUseCase
    ): IRankViewModel {
        val factory = ViewModelFactory.MainScreen.Rank(
            fragment, interactor, getList, insertRank, deleteRank, updateRank, correctPositions
        )
        return ViewModelProvider(fragment, factory)[RankViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideNotesViewModel(
        fragment: NotesFragment,
        preferencesRepo: PreferencesRepo,
        interactor: INotesInteractor,
        getList: GetNoteListUseCase,
        sortList: SortNoteListUseCase,
        getCopyText: GetCopyTextUseCase,
        updateNote: UpdateNoteUseCase,
        deleteNote: DeleteNoteUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        getNotificationDateList: GetNotificationDateListUseCase
    ): INotesViewModel {
        val factory = ViewModelFactory.MainScreen.Notes(
            fragment, preferencesRepo, interactor,
            getList, sortList, getCopyText, updateNote, deleteNote, setNotification,
            deleteNotification, getNotificationDateList
        )
        return ViewModelProvider(fragment, factory)[NotesViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideBinViewModel(
        fragment: BinFragment,
        interactor: IBinInteractor,
        getList: GetNoteListUseCase,
        getCopyText: GetCopyTextUseCase,
        restoreNote: RestoreNoteUseCase,
        clearBin: ClearBinUseCase,
        clearNote: ClearNoteUseCase
    ): IBinViewModel {
        val factory = ViewModelFactory.MainScreen.Bin(
            fragment, interactor, getList, getCopyText, restoreNote, clearBin, clearNote
        )
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
        updateNote: UpdateNoteUseCase,
        deleteNote: DeleteNoteUseCase,
        restoreNote: RestoreNoteUseCase,
        clearNote: ClearNoteUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        getNotificationDateList: GetNotificationDateListUseCase,
        getRankId: GetRankIdUseCase,
        getRankDialogNames: GetRankDialogNamesUseCase
    ): ITextNoteViewModel {
        val factory = ViewModelFactory.NoteScreen.TextNote(
            fragment, colorConverter, preferencesRepo, interactor,
            updateNote, deleteNote, restoreNote, clearNote, setNotification, deleteNotification,
            getNotificationDateList, getRankId, getRankDialogNames
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
        updateNote: UpdateNoteUseCase,
        deleteNote: DeleteNoteUseCase,
        restoreNote: RestoreNoteUseCase,
        clearNote: ClearNoteUseCase,
        updateVisible: UpdateRollVisibleUseCase,
        updateCheck: UpdateRollCheckUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        getNotificationDateList: GetNotificationDateListUseCase,
        getRankId: GetRankIdUseCase,
        getRankDialogNames: GetRankDialogNamesUseCase,
    ): IRollNoteViewModel {
        val factory = ViewModelFactory.NoteScreen.RollNote(
            fragment, colorConverter, preferencesRepo, interactor,
            updateNote, deleteNote, restoreNote, clearNote, updateVisible, updateCheck,
            setNotification, deleteNotification, getNotificationDateList, getRankId,
            getRankDialogNames
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
        noteRepo: NoteRepo,
        getMelodyList: GetMelodyListUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        shiftDateIfExist: ShiftDateIfExistUseCase
    ): AlarmViewModel {
        val factory = ViewModelFactory.Alarm(
            preferencesRepo, noteRepo, getMelodyList,
            setNotification, deleteNotification, shiftDateIfExist
        )

        return ViewModelProvider(activity, factory)[AlarmViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideNotificationViewModel(
        activity: NotificationActivity,
        interactor: INotificationInteractor,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        getList: GetNotificationListUseCase
    ): INotificationViewModel {
        val factory = ViewModelFactory.Notification(
            activity, interactor, setNotification, deleteNotification, getList
        )
        return ViewModelProvider(activity, factory)[NotificationViewModel::class.java]
    }

    //region Preference

    @Provides
    @ActivityScope
    fun providePreferenceViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo,
        @Named("Theme") getSummary: GetSummaryUseCase
    ): MenuPreferenceViewModel {
        val factory = ViewModelFactory.Preference.Main(preferencesRepo, getSummary)
        return ViewModelProvider(owner, factory)[MenuPreferenceViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideBackupPreferenceViewModel(
        fragment: BackupPreferenceFragment,
        getBackupFileList: GetBackupFileListUseCase,
        startBackupExport: StartBackupExportUseCase,
        startBackupImport: StartBackupImportUseCase
    ): IBackupPreferenceViewModel {
        val factory = ViewModelFactory.Preference.Backup(
            fragment, getBackupFileList, startBackupExport, startBackupImport
        )
        return ViewModelProvider(fragment, factory)[BackupPreferenceViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideNotePreferenceViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo,
        @Named("Sort") getSortSummary: GetSummaryUseCase,
        @Named("DefaultColor") getDefaultColorSummary: GetSummaryUseCase,
        @Named("SavePeriod") getSavePeriodSummary: GetSummaryUseCase
    ): NotePreferenceViewModel {
        val factory = ViewModelFactory.Preference.Note(
            preferencesRepo, getSortSummary, getDefaultColorSummary, getSavePeriodSummary
        )

        return ViewModelProvider(owner, factory)[NotePreferenceViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideAlarmPreferenceViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo,
        @Named("Repeat") getRepeatSummary: GetSummaryUseCase,
        getSignalSummary: GetSignalSummaryUseCase,
        @Named("Volume") getVolumeSummary: GetSummaryUseCase,
        getMelodyList: GetMelodyListUseCase
    ): IAlarmPreferenceViewModel {
        val factory = ViewModelFactory.Preference.Alarm(
            preferencesRepo, getRepeatSummary, getSignalSummary, getVolumeSummary,
            getMelodyList
        )
        return ViewModelProvider(owner, factory)[AlarmPreferenceViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideDevelopViewModel(
        owner: ViewModelStoreOwner,
        interactor: DevelopInteractor
    ): DevelopViewModel {
        val factory = ViewModelFactory.Develop.Main(interactor)
        return ViewModelProvider(owner, factory)[DevelopViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun providePrintDevelopViewModel(
        activity: PrintDevelopActivity,
        interactor: DevelopInteractor
    ): IPrintDevelopViewModel {
        val factory = ViewModelFactory.Develop.Print(activity, interactor)
        return ViewModelProvider(activity, factory)[PrintDevelopViewModel::class.java]
    }

    @Provides
    @ActivityScope
    fun provideServiceDevelopViewModel(owner: ViewModelStoreOwner): ServiceDevelopViewModel {
        val factory = ViewModelFactory.Develop.Service()
        return ViewModelProvider(owner, factory)[ServiceDevelopViewModelImpl::class.java]
    }

    //endregion

}