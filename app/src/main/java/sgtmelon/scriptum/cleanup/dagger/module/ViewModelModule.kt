package sgtmelon.scriptum.cleanup.dagger.module

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dagger.Module
import dagger.Provides
import javax.inject.Named
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.other.ViewModelFactory
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.cleanup.presentation.control.note.save.SaveControlImpl
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.develop.domain.GetPrintListUseCase
import sgtmelon.scriptum.develop.domain.GetRandomNoteIdUseCase
import sgtmelon.scriptum.develop.domain.ResetPreferencesUseCase
import sgtmelon.scriptum.develop.infrastructure.model.PrintType
import sgtmelon.scriptum.develop.infrastructure.screen.develop.DevelopViewModel
import sgtmelon.scriptum.develop.infrastructure.screen.develop.DevelopViewModelImpl
import sgtmelon.scriptum.develop.infrastructure.screen.print.PrintDevelopViewModel
import sgtmelon.scriptum.develop.infrastructure.screen.print.PrintDevelopViewModelImpl
import sgtmelon.scriptum.develop.infrastructure.screen.service.ServiceDevelopViewModel
import sgtmelon.scriptum.develop.infrastructure.screen.service.ServiceDevelopViewModelImpl
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.ShiftDateIfExistUseCase
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupExportUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupImportUseCase
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.main.GetBinListUseCase
import sgtmelon.scriptum.domain.useCase.main.GetNotesListUseCase
import sgtmelon.scriptum.domain.useCase.main.SortNoteListUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.ConvertNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.SaveNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateRollCheckUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateRollVisibleUseCase
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateRollNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.createNote.CreateTextNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.getNote.GetRollNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.getNote.GetTextNoteUseCase
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
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult
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
import sgtmelon.scriptum.infrastructure.screen.note.NoteViewModel
import sgtmelon.scriptum.infrastructure.screen.note.NoteViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteViewModel
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteViewModel
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsViewModel
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.backup.BackupPreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.backup.BackupPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceViewModel
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceViewModelImpl
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
        owner: ViewModelStoreOwner,
        getList: GetRankListUseCase,
        insertRank: InsertRankUseCase,
        deleteRank: DeleteRankUseCase,
        updateRank: UpdateRankUseCase,
        correctRankPositions: CorrectRankPositionsUseCase,
        updateRankPositions: UpdateRankPositionsUseCase
    ): RankViewModel {
        val factory = ViewModelFactory.MainScreen.Rank(
            getList, insertRank, deleteRank, updateRank, correctRankPositions, updateRankPositions
        )
        return ViewModelProvider(owner, factory)[RankViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideNotesViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo,
        getList: GetNotesListUseCase,
        sortList: SortNoteListUseCase,
        getCopyText: GetCopyTextUseCase,
        convertNote: ConvertNoteUseCase,
        updateNote: UpdateNoteUseCase,
        deleteNote: DeleteNoteUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        getNotificationDateList: GetNotificationDateListUseCase
    ): NotesViewModel {
        val factory = ViewModelFactory.MainScreen.Notes(
            preferencesRepo, getList, sortList, getCopyText, convertNote, updateNote, deleteNote,
            setNotification, deleteNotification, getNotificationDateList
        )
        return ViewModelProvider(owner, factory)[NotesViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideBinViewModel(
        owner: ViewModelStoreOwner,
        getList: GetBinListUseCase,
        getCopyText: GetCopyTextUseCase,
        restoreNote: RestoreNoteUseCase,
        clearBin: ClearBinUseCase,
        clearNote: ClearNoteUseCase
    ): BinViewModel {
        val factory = ViewModelFactory.MainScreen.Bin(
            getList, getCopyText, restoreNote, clearBin, clearNote
        )
        return ViewModelProvider(owner, factory)[BinViewModelImpl::class.java]
    }

    //endregion

    //region Note

    @Provides
    @ActivityScope
    fun provideNoteViewModel(owner: ViewModelStoreOwner): NoteViewModel {
        val factory = ViewModelFactory.NoteScreen.Note()
        return ViewModelProvider(owner, factory)[NoteViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun provideTextNoteViewModel(
        init: NoteInit,
        inputControl: IInputControl,
        createNote: CreateTextNoteUseCase,
        getNote: GetTextNoteUseCase,

        // TODO refactor
        fragment: TextNoteFragmentImpl,
        colorConverter: ColorConverter,
        preferencesRepo: PreferencesRepo,
        saveNote: SaveNoteUseCase,
        convertNote: ConvertNoteUseCase,
        updateNote: UpdateNoteUseCase,
        deleteNote: DeleteNoteUseCase,
        restoreNote: RestoreNoteUseCase,
        clearNote: ClearNoteUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        getNotificationDateList: GetNotificationDateListUseCase,
        getRankId: GetRankIdUseCase,
        getRankDialogNames: GetRankDialogNamesUseCase
    ): TextNoteViewModel {
        val factory = ViewModelFactory.NoteScreen.TextNote(
            init, inputControl, createNote, getNote,

            // TODO cleanup
            fragment, colorConverter, preferencesRepo, saveNote, convertNote,
            updateNote, deleteNote, restoreNote, clearNote, setNotification, deleteNotification,
            getNotificationDateList, getRankId, getRankDialogNames
        )
        val viewModel = ViewModelProvider(fragment, factory)[TextNoteViewModelImpl::class.java]
        val saveControl = SaveControlImpl(fragment.resources, preferencesRepo.saveState, viewModel)
        viewModel.setSaveControl(saveControl)

        return viewModel
    }

    @Provides
    @ActivityScope
    fun provideRollNoteViewModel(
        init: NoteInit,
        inputControl: IInputControl,
        createNote: CreateRollNoteUseCase,
        getNote: GetRollNoteUseCase,

        // TODO refactor
        fragment: RollNoteFragmentImpl,
        colorConverter: ColorConverter,
        preferencesRepo: PreferencesRepo,
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
        getNotificationDateList: GetNotificationDateListUseCase,
        getRankId: GetRankIdUseCase,
        getRankDialogNames: GetRankDialogNamesUseCase,
    ): RollNoteViewModel {
        val factory = ViewModelFactory.NoteScreen.RollNote(
            init, inputControl, createNote, getNote,

            // TODO cleanup
            fragment, colorConverter, preferencesRepo, saveNote, convertNote,
            updateNote, deleteNote, restoreNote, clearNote, updateVisible, updateCheck,
            setNotification, deleteNotification, getNotificationDateList, getRankId,
            getRankDialogNames
        )
        val viewModel = ViewModelProvider(fragment, factory)[RollNoteViewModelImpl::class.java]
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
    ): NotificationsViewModel {
        val factory = ViewModelFactory.Notification(setNotification, deleteNotification, getList)
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
    fun provideNotesPreferenceViewModel(
        owner: ViewModelStoreOwner,
        preferencesRepo: PreferencesRepo,
        @Named("Sort") getSortSummary: GetSummaryUseCase,
        @Named("DefaultColor") getDefaultColorSummary: GetSummaryUseCase,
        @Named("SavePeriod") getSavePeriodSummary: GetSummaryUseCase
    ): NotesPreferenceViewModel {
        val factory = ViewModelFactory.Preference.Note(
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
        getRandomNoteId: GetRandomNoteIdUseCase,
        resetPreferences: ResetPreferencesUseCase
    ): DevelopViewModel {
        val factory = ViewModelFactory.Develop.Main(getRandomNoteId, resetPreferences)
        return ViewModelProvider(owner, factory)[DevelopViewModelImpl::class.java]
    }

    @Provides
    @ActivityScope
    fun providePrintDevelopViewModel(
        owner: ViewModelStoreOwner,
        type: PrintType,
        getList: GetPrintListUseCase
    ): PrintDevelopViewModel {
        val factory = ViewModelFactory.Develop.Print(type, getList)
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