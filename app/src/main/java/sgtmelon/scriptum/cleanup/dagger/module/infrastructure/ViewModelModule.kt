package sgtmelon.scriptum.cleanup.dagger.module.infrastructure

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.other.ViewModelFactory
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.develop.domain.GetPrintListUseCase
import sgtmelon.scriptum.develop.domain.GetRandomNoteIdUseCase
import sgtmelon.scriptum.develop.domain.ResetPreferencesUseCase
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.develop.infrastructure.model.PrintType
import sgtmelon.scriptum.develop.infrastructure.screen.develop.DevelopViewModel
import sgtmelon.scriptum.develop.infrastructure.screen.develop.DevelopViewModelImpl
import sgtmelon.scriptum.develop.infrastructure.screen.print.PrintDevelopViewModel
import sgtmelon.scriptum.develop.infrastructure.screen.print.PrintDevelopViewModelImpl
import sgtmelon.scriptum.develop.infrastructure.screen.service.ServiceDevelopViewModel
import sgtmelon.scriptum.develop.infrastructure.screen.service.ServiceDevelopViewModelImpl
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationsDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.ShiftDateIfExistUseCase
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupExportUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupImportUseCase
import sgtmelon.scriptum.domain.useCase.files.GetSavePathUseCase
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.main.GetBinListUseCase
import sgtmelon.scriptum.domain.useCase.main.GetNotesListUseCase
import sgtmelon.scriptum.domain.useCase.main.SortNoteListUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.GetHistoryResultUseCase
import sgtmelon.scriptum.domain.useCase.note.GetNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.SaveNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateRollCheckUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateRollVisibleUseCase
import sgtmelon.scriptum.domain.useCase.note.cacheNote.CacheRollNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.cacheNote.CacheTextNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateNoteUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.domain.useCase.rank.CorrectRankPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.DeleteRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankListUseCase
import sgtmelon.scriptum.domain.useCase.rank.InsertRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmViewModel
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.MainViewModel
import sgtmelon.scriptum.infrastructure.screen.main.MainViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinViewModel
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesViewModel
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankViewModel
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteViewModel
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteViewModel
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsViewModel
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListStorageImpl
import sgtmelon.scriptum.infrastructure.screen.parent.permission.PermissionViewModel
import sgtmelon.scriptum.infrastructure.screen.parent.permission.PermissionViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.backup.BackupPreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.backup.BackupPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.splash.SplashViewModel
import sgtmelon.scriptum.infrastructure.screen.splash.SplashViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeViewModel
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeViewModelImpl
import javax.inject.Named

@Module
class ViewModelModule {

    @Provides
    @ActivityScope
    fun provideSplashViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo,
        createNote: CreateNoteUseCase
    ): SplashViewModel {
        val factory = ViewModelFactory.getSplash(preferencesRepo, createNote)
        return ViewModelProvider(owner, factory)[SplashViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideThemeViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo
    ): ThemeViewModel {
        val factory = ViewModelFactory.getTheme(preferencesRepo)
        return ViewModelProvider(owner, factory)[ThemeViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun providePermissionViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo
    ): PermissionViewModel {
        val factory = ViewModelFactory.getPermission(preferencesRepo)
        return ViewModelProvider(owner, factory)[PermissionViewModelImpl::class.java]
    }


    //region Main

    @Provides
    @ActivityScope
    fun provideMainViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo,
        createNote: CreateNoteUseCase
    ): MainViewModel {
        val factory = ViewModelFactory.MainScreen.getMain(preferencesRepo, createNote)
        return ViewModelProvider(owner, factory)[MainViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideRankViewModel(
        owner: ViewModelStoreOwner,
        @Named("Rank") list: ListStorageImpl<RankItem>,
        getList: GetRankListUseCase,
        insertRank: InsertRankUseCase,
        deleteRank: DeleteRankUseCase,
        updateRank: UpdateRankUseCase,
        correctRankPositions: CorrectRankPositionsUseCase,
        updateRankPositions: UpdateRankPositionsUseCase
    ): RankViewModel {
        val factory = ViewModelFactory.MainScreen.getRank(
            list, getList, insertRank, deleteRank, updateRank, correctRankPositions,
            updateRankPositions
        )
        return ViewModelProvider(owner, factory)[RankViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideNotesViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo,
        @Named("Note") list: ListStorageImpl<NoteItem>,
        getList: GetNotesListUseCase,
        sortList: SortNoteListUseCase,
        getCopyText: GetCopyTextUseCase,
        convertNote: ConvertNoteUseCase,
        updateNote: UpdateNoteUseCase,
        deleteNote: DeleteNoteUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        getNotificationDateList: GetNotificationsDateListUseCase
    ): NotesViewModel {
        val factory = ViewModelFactory.MainScreen.getNotes(
            preferencesRepo, list,
            getList, sortList, getCopyText, convertNote, updateNote, deleteNote,
            setNotification, deleteNotification, getNotificationDateList
        )
        return ViewModelProvider(owner, factory)[NotesViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideBinViewModel(
        owner: ViewModelStoreOwner,
        @Named("Note") list: ListStorageImpl<NoteItem>,
        getList: GetBinListUseCase,
        getCopyText: GetCopyTextUseCase,
        restoreNote: RestoreNoteUseCase,
        clearBin: ClearBinUseCase,
        clearNote: ClearNoteUseCase
    ): BinViewModel {
        val factory = ViewModelFactory.MainScreen.getBin(
            list, getList, getCopyText, restoreNote, clearBin, clearNote
        )
        return ViewModelProvider(owner, factory)[BinViewModelImpl::class.java]
    }

    //endregion

    //region Note

    @Provides
    @ActivityScope
    fun provideTextNoteViewModel(
        owner: ViewModelStoreOwner,
        init: NoteInit,
        history: NoteHistory,
        colorConverter: ColorConverter,
        cacheNote: CacheTextNoteUseCase,
        saveNote: SaveNoteUseCase,
        convertNote: ConvertNoteUseCase,
        updateNote: UpdateNoteUseCase,
        deleteNote: DeleteNoteUseCase,
        restoreNote: RestoreNoteUseCase,
        clearNote: ClearNoteUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        getNotificationDateList: GetNotificationsDateListUseCase,
        getRankId: GetRankIdUseCase,
        getRankDialogNames: GetRankDialogNamesUseCase,
        getHistoryResult: GetHistoryResultUseCase
    ): TextNoteViewModel {
        val factory = ViewModelFactory.NoteScreen.getTextNote(
            init, history, colorConverter, cacheNote,
            saveNote, convertNote, updateNote, deleteNote, restoreNote, clearNote,
            setNotification, deleteNotification, getNotificationDateList,
            getRankId, getRankDialogNames, getHistoryResult
        )

        return ViewModelProvider(owner, factory)[TextNoteViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideRollNoteViewModel(
        owner: ViewModelStoreOwner,
        init: NoteInit,
        history: NoteHistory,
        colorConverter: ColorConverter,
        @Named("Roll") list: ListStorageImpl<RollItem>,
        cacheNote: CacheRollNoteUseCase,
        saveNote: SaveNoteUseCase,
        convertNote: ConvertNoteUseCase,
        updateNote: UpdateNoteUseCase,
        deleteNote: DeleteNoteUseCase,
        restoreNote: RestoreNoteUseCase,
        clearNote: ClearNoteUseCase,
        updateVisible: UpdateRollVisibleUseCase,
        updateCheck: UpdateRollCheckUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        getNotificationDateList: GetNotificationsDateListUseCase,
        getRankId: GetRankIdUseCase,
        getRankDialogNames: GetRankDialogNamesUseCase,
        getHistoryResult: GetHistoryResultUseCase
    ): RollNoteViewModel {
        val factory = ViewModelFactory.NoteScreen.getRollNote(
            init, history, colorConverter, list, cacheNote,
            saveNote, convertNote, updateNote, deleteNote, restoreNote, clearNote,
            updateVisible, updateCheck,
            setNotification, deleteNotification, getNotificationDateList,
            getRankId, getRankDialogNames, getHistoryResult
        )

        return ViewModelProvider(owner, factory)[RollNoteViewModelImpl::class.java]
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideAlarmViewModel(
        owner: ViewModelStoreOwner,
        noteId: Long,
        preferencesRepo: PreferencesRepo,
        getNote: GetNoteUseCase,
        getMelodyList: GetMelodyListUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        shiftDateIfExist: ShiftDateIfExistUseCase
    ): AlarmViewModel {
        val factory = ViewModelFactory.getAlarm(
            noteId, preferencesRepo, getNote, getMelodyList,
            setNotification, deleteNotification, shiftDateIfExist
        )

        return ViewModelProvider(owner, factory)[AlarmViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideNotificationViewModel(
        owner: ViewModelStoreOwner,
        @Named("Notification") list: ListStorageImpl<NotificationItem>,
        getList: GetNotificationListUseCase,
        getNote: GetNoteUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase
    ): NotificationsViewModel {
        val factory = ViewModelFactory.getNotification(
            list, getList, getNote, setNotification, deleteNotification
        )
        return ViewModelProvider(owner, factory)[NotificationsViewModelImpl::class.java]
    }

    //region Preference

    @Provides
    @ActivityScope
    fun providePreferenceViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo,
        @Named("Theme") getSummary: GetSummaryUseCase
    ): MenuPreferenceViewModel {
        val factory = ViewModelFactory.Preference.getMain(preferencesRepo, getSummary)
        return ViewModelProvider(owner, factory)[MenuPreferenceViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideBackupPreferenceViewModel(
        owner: ViewModelStoreOwner,
        isFilesAutoFetch: Boolean,
        getSavePath: GetSavePathUseCase,
        getBackupFileList: GetBackupFileListUseCase,
        startBackupExport: StartBackupExportUseCase,
        startBackupImport: StartBackupImportUseCase
    ): BackupPreferenceViewModel {
        val factory = ViewModelFactory.Preference.getBackup(
            isFilesAutoFetch, getSavePath, getBackupFileList, startBackupExport, startBackupImport
        )
        return ViewModelProvider(owner, factory)[BackupPreferenceViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideNotesPreferenceViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo,
        @Named("Sort") getSortSummary: GetSummaryUseCase,
        @Named("DefaultColor") getDefaultColorSummary: GetSummaryUseCase,
        @Named("SavePeriod") getSavePeriodSummary: GetSummaryUseCase
    ): NotesPreferenceViewModel {
        val factory = ViewModelFactory.Preference.getNote(
            preferencesRepo, getSortSummary, getDefaultColorSummary, getSavePeriodSummary
        )

        return ViewModelProvider(owner, factory)[NotesPreferenceViewModelImpl::class.java]
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
        val factory = ViewModelFactory.Preference.getAlarm(
            preferencesRepo, getSignalSummary, getRepeatSummary, getVolumeSummary,
            getMelodyList
        )
        return ViewModelProvider(owner, factory)[AlarmPreferenceViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideDevelopViewModel(
        owner: ViewModelStoreOwner,
        getRandomNoteId: GetRandomNoteIdUseCase,
        resetPreferences: ResetPreferencesUseCase
    ): DevelopViewModel {
        val factory = ViewModelFactory.Develop.getMain(getRandomNoteId, resetPreferences)
        return ViewModelProvider(owner, factory)[DevelopViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun providePrintDevelopViewModel(
        owner: ViewModelStoreOwner,
        type: PrintType,
        @Named("Print") list: ListStorageImpl<PrintItem>,
        getList: GetPrintListUseCase
    ): PrintDevelopViewModel {
        val factory = ViewModelFactory.Develop.getPrint(type, list, getList)
        return ViewModelProvider(owner, factory)[PrintDevelopViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideServiceDevelopViewModel(owner: ViewModelStoreOwner): ServiceDevelopViewModel {
        val factory = ViewModelFactory.Develop.getService()
        return ViewModelProvider(owner, factory)[ServiceDevelopViewModelImpl::class.java]
    }

    //endregion

}