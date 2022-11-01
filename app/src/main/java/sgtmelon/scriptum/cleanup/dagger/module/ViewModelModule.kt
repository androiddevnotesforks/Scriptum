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
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControlImpl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IIntroViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.main.IRankViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.IRollNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.IntroViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.NotesViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.RankViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.NoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.RollNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.TextNoteViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.develop.model.PrintType
import sgtmelon.scriptum.develop.screen.develop.DevelopViewModel
import sgtmelon.scriptum.develop.screen.develop.DevelopViewModelImpl
import sgtmelon.scriptum.develop.screen.print.PrintDevelopViewModel
import sgtmelon.scriptum.develop.screen.print.PrintDevelopViewModelImpl
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
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmViewModel
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.MainViewModel
import sgtmelon.scriptum.infrastructure.screen.main.MainViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinViewModel
import sgtmelon.scriptum.infrastructure.screen.main.bin.IBinViewModel
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationViewModel
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.backup.BackupPreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.backup.BackupPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.splash.ISplashViewModel
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.infrastructure.screen.splash.SplashViewModel
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
    fun provideMainViewModel(owner: ViewModelStoreOwner): MainViewModel {
        val factory = ViewModelFactory.MainScreen.Main()
        return ViewModelProvider(owner, factory)[MainViewModelImpl::class.java]
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
        owner: ViewModelStoreOwner,
        noteId: Long,
        preferencesRepo: PreferencesRepo,
        noteRepo: NoteRepo,
        getMelodyList: GetMelodyListUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        shiftDateIfExist: ShiftDateIfExistUseCase
    ): AlarmViewModel {
        val factory = ViewModelFactory.Alarm(
            noteId, preferencesRepo, noteRepo, getMelodyList,
            setNotification, deleteNotification, shiftDateIfExist
        )

        return ViewModelProvider(owner, factory)[AlarmViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideNotificationViewModel(
        owner: ViewModelStoreOwner,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        getList: GetNotificationListUseCase
    ): NotificationViewModel {
        val factory = ViewModelFactory.Notification(setNotification, deleteNotification, getList)
        return ViewModelProvider(owner, factory)[NotificationViewModelImpl::class.java]
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
        owner: ViewModelStoreOwner,
        permissionResult: PermissionResult?,
        getBackupFileList: GetBackupFileListUseCase,
        startBackupExport: StartBackupExportUseCase,
        startBackupImport: StartBackupImportUseCase
    ): BackupPreferenceViewModel {
        val factory = ViewModelFactory.Preference.Backup(
            permissionResult, getBackupFileList, startBackupExport, startBackupImport
        )
        return ViewModelProvider(owner, factory)[BackupPreferenceViewModelImpl::class.java]
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
        getSignalSummary: GetSignalSummaryUseCase,
        @Named("Repeat") getRepeatSummary: GetSummaryUseCase,
        @Named("Volume") getVolumeSummary: GetSummaryUseCase,
        getMelodyList: GetMelodyListUseCase
    ): AlarmPreferenceViewModel {
        val factory = ViewModelFactory.Preference.Alarm(
            preferencesRepo, getSignalSummary, getRepeatSummary, getVolumeSummary,
            getMelodyList
        )
        return ViewModelProvider(owner, factory)[AlarmPreferenceViewModelImpl::class.java]
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
        owner: ViewModelStoreOwner,
        type: PrintType,
        interactor: DevelopInteractor
    ): PrintDevelopViewModel {
        val factory = ViewModelFactory.Develop.Print(type, interactor)
        return ViewModelProvider(owner, factory)[PrintDevelopViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideServiceDevelopViewModel(owner: ViewModelStoreOwner): ServiceDevelopViewModel {
        val factory = ViewModelFactory.Develop.Service()
        return ViewModelProvider(owner, factory)[ServiceDevelopViewModelImpl::class.java]
    }

    //endregion

}