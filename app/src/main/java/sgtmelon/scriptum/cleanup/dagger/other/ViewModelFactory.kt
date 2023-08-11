package sgtmelon.scriptum.cleanup.dagger.other

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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
import sgtmelon.scriptum.develop.infrastructure.screen.develop.DevelopViewModelImpl
import sgtmelon.scriptum.develop.infrastructure.screen.print.PrintDevelopViewModelImpl
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
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.MainViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListStorageImpl
import sgtmelon.scriptum.infrastructure.screen.parent.permission.PermissionViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.backup.BackupPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.splash.SplashViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeViewModelImpl

/**
 * ViewModel factory for create ViewModels with constructor parameters.
 */
object ViewModelFactory {

    fun getSplash(
        preferencesRepo: PreferencesRepo,
        createNote: CreateNoteUseCase
    ): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            SplashViewModelImpl(preferencesRepo, createNote)
        }
    }

    fun getTheme(preferencesRepo: PreferencesRepo): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            ThemeViewModelImpl(preferencesRepo)
        }
    }

    fun getPermission(preferencesRepo: PreferencesRepo): ViewModelProvider.Factory {
        return viewModelFactory { initializer { PermissionViewModelImpl(preferencesRepo) } }
    }

    object MainScreen {

        fun getMain(
            preferencesRepo: PreferencesRepo,
            createNote: CreateNoteUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModelImpl(preferencesRepo, createNote)
            }
        }

        fun getRank(
            list: ListStorageImpl<RankItem>,
            getList: GetRankListUseCase,
            insertRank: InsertRankUseCase,
            deleteRank: DeleteRankUseCase,
            updateRank: UpdateRankUseCase,
            correctRankPositions: CorrectRankPositionsUseCase,
            updateRankPositions: UpdateRankPositionsUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                RankViewModelImpl(
                    list, getList, insertRank, deleteRank, updateRank,
                    correctRankPositions, updateRankPositions
                )
            }
        }

        fun getNotes(
            preferencesRepo: PreferencesRepo,
            list: ListStorageImpl<NoteItem>,
            getList: GetNotesListUseCase,
            sortList: SortNoteListUseCase,
            getCopyText: GetCopyTextUseCase,
            convertNote: ConvertNoteUseCase,
            updateNote: UpdateNoteUseCase,
            deleteNote: DeleteNoteUseCase,
            setNotification: SetNotificationUseCase,
            deleteNotification: DeleteNotificationUseCase,
            getNotificationDateList: GetNotificationsDateListUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                NotesViewModelImpl(
                    preferencesRepo, list,
                    getList, sortList, getCopyText, convertNote, updateNote,
                    deleteNote, setNotification, deleteNotification, getNotificationDateList
                )
            }
        }

        fun getBin(
            list: ListStorageImpl<NoteItem>,
            getList: GetBinListUseCase,
            getCopyText: GetCopyTextUseCase,
            restoreNote: RestoreNoteUseCase,
            clearBin: ClearBinUseCase,
            clearNote: ClearNoteUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                BinViewModelImpl(list, getList, getCopyText, restoreNote, clearBin, clearNote)
            }
        }
    }

    object NoteScreen {

        fun getTextNote(
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
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TextNoteViewModelImpl(
                    colorConverter, init, history, cacheNote,
                    saveNote, convertNote, updateNote, deleteNote, restoreNote, clearNote,
                    setNotification, deleteNotification, getNotificationDateList,
                    getRankId, getRankDialogNames, getHistoryResult
                )
            }
        }

        fun getRollNote(
            init: NoteInit,
            history: NoteHistory,
            colorConverter: ColorConverter,
            list: ListStorageImpl<RollItem>,
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
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                RollNoteViewModelImpl(
                    init, history, colorConverter, list, cacheNote,
                    saveNote, convertNote, updateNote, deleteNote, restoreNote, clearNote,
                    updateVisible, updateCheck,
                    setNotification, deleteNotification, getNotificationDateList,
                    getRankId, getRankDialogNames, getHistoryResult
                )
            }
        }
    }

    fun getAlarm(
        noteId: Long,
        preferencesRepo: PreferencesRepo,
        getNote: GetNoteUseCase,
        getMelodyList: GetMelodyListUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase,
        shiftDateIfExist: ShiftDateIfExistUseCase
    ): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            AlarmViewModelImpl(
                noteId, preferencesRepo, getNote, getMelodyList,
                setNotification, deleteNotification, shiftDateIfExist
            )
        }
    }

    fun getNotification(
        list: ListStorageImpl<NotificationItem>,
        getList: GetNotificationListUseCase,
        getNote: GetNoteUseCase,
        setNotification: SetNotificationUseCase,
        deleteNotification: DeleteNotificationUseCase
    ): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            NotificationsViewModelImpl(list, getList, getNote, setNotification, deleteNotification)
        }
    }

    object Preference {

        fun getMain(
            preferencesRepo: PreferencesRepo,
            getSummary: GetSummaryUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MenuPreferenceViewModelImpl(preferencesRepo, getSummary)
            }
        }

        fun getBackup(
            isFilesAutoFetch: Boolean,
            getSavePath: GetSavePathUseCase,
            getBackupFileList: GetBackupFileListUseCase,
            startBackupExport: StartBackupExportUseCase,
            startBackupImport: StartBackupImportUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                BackupPreferenceViewModelImpl(
                    isFilesAutoFetch, getSavePath, getBackupFileList, startBackupExport,
                    startBackupImport
                )
            }
        }

        fun getNote(
            preferencesRepo: PreferencesRepo,
            getSortSummary: GetSummaryUseCase,
            getDefaultColorSummary: GetSummaryUseCase,
            getSavePeriodSummary: GetSummaryUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                NotesPreferenceViewModelImpl(
                    preferencesRepo,
                    getSortSummary, getDefaultColorSummary, getSavePeriodSummary
                )
            }
        }

        fun getAlarm(
            preferencesRepo: PreferencesRepo,
            getSignalSummary: GetSignalSummaryUseCase,
            getRepeatSummary: GetSummaryUseCase,
            getVolumeSummary: GetSummaryUseCase,
            getMelodyList: GetMelodyListUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AlarmPreferenceViewModelImpl(
                    preferencesRepo, getSignalSummary, getRepeatSummary, getVolumeSummary,
                    getMelodyList
                )
            }
        }
    }

    object Develop {

        fun getMain(
            getRandomNoteId: GetRandomNoteIdUseCase,
            resetPreferences: ResetPreferencesUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DevelopViewModelImpl(getRandomNoteId, resetPreferences)
            }
        }

        fun getPrint(
            type: PrintType,
            list: ListStorageImpl<PrintItem>,
            getList: GetPrintListUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PrintDevelopViewModelImpl(type, list, getList)
            }
        }

        fun getService(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ServiceDevelopViewModelImpl()
            }
        }
    }
}