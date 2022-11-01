package sgtmelon.scriptum.cleanup.dagger.other

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.IntroViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.NotesViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.RankViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.NoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.RollNoteViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.TextNoteViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.develop.model.PrintType
import sgtmelon.scriptum.develop.screen.develop.DevelopViewModelImpl
import sgtmelon.scriptum.develop.screen.print.PrintDevelopViewModelImpl
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
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.MainViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.backup.BackupPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.splash.SplashViewModel
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeViewModelImpl

/**
 * ViewModel factory for create ViewModels with constructor parameters.
 */
@Suppress("UNCHECKED_CAST")
object ViewModelFactory {

    //region Help func

    private fun onNotFound() = IllegalArgumentException("ViewModel Not Found")

    private inline fun <T> Class<T>.create(modelClass: KClass<*>, createFunc: () -> Any): T {
        return if (isAssignableFrom(modelClass.java)) createFunc() as T else throw onNotFound()
    }

    //endregion

    class Theme(private val preferencesRepo: PreferencesRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create(ThemeViewModelImpl::class) {
                ThemeViewModelImpl(preferencesRepo)
            }
        }
    }

    class Splash(private val preferencesRepo: PreferencesRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create(SplashViewModel::class) {
                SplashViewModel(preferencesRepo)
            }
        }
    }

    class Intro(
        private val activity: IntroActivity,
        private val preferencesRepo: PreferencesRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create(IntroViewModel::class) {
                IntroViewModel(activity, preferencesRepo)
            }
        }
    }

    object MainScreen {

        class Main() : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(MainViewModelImpl::class) {
                    MainViewModelImpl()
                }
            }
        }

        class Rank(
            private val fragment: RankFragment,
            private val interactor: IRankInteractor,
            private val getList: GetRankListUseCase,
            private val insertRank: InsertRankUseCase,
            private val deleteRank: DeleteRankUseCase,
            private val updateRank: UpdateRankUseCase,
            private val correctPositions: CorrectPositionsUseCase
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(RankViewModel::class) {
                    RankViewModel(
                        fragment, interactor, getList, insertRank, deleteRank, updateRank,
                        correctPositions
                    )
                }
            }
        }

        class Notes(
            private val fragment: NotesFragment,
            private val preferencesRepo: PreferencesRepo,
            private val interactor: INotesInteractor,
            private val getList: GetNoteListUseCase,
            private val sortList: SortNoteListUseCase,
            private val getCopyText: GetCopyTextUseCase,
            private val updateNote: UpdateNoteUseCase,
            private val deleteNote: DeleteNoteUseCase,
            private val setNotification: SetNotificationUseCase,
            private val deleteNotification: DeleteNotificationUseCase,
            private val getNotificationDateList: GetNotificationDateListUseCase
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(NotesViewModel::class) {
                    NotesViewModel(
                        fragment, preferencesRepo, interactor,
                        getList, sortList, getCopyText, updateNote, deleteNote, setNotification,
                        deleteNotification, getNotificationDateList
                    )
                }
            }
        }

        class Bin(
            private val fragment: BinFragment,
            private val interactor: IBinInteractor,
            private val getList: GetNoteListUseCase,
            private val getCopyText: GetCopyTextUseCase,
            private val restoreNote: RestoreNoteUseCase,
            private val clearBin: ClearBinUseCase,
            private val clearNote: ClearNoteUseCase
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(BinViewModelImpl::class) {
                    BinViewModelImpl(
                        fragment, interactor,
                        getList, getCopyText, restoreNote, clearBin, clearNote
                    )
                }
            }
        }
    }

    object NoteScreen {

        class Note(
            private val activity: NoteActivity,
            private val typeConverter: NoteTypeConverter,
            private val colorConverter: ColorConverter,
            private val preferencesRepo: PreferencesRepo
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(NoteViewModel::class) {
                    NoteViewModel(activity, typeConverter, colorConverter, preferencesRepo)
                }
            }
        }

        class TextNote(
            private val fragment: TextNoteFragment,
            private val colorConverter: ColorConverter,
            private val preferencesRepo: PreferencesRepo,
            private val interactor: ITextNoteInteractor,
            private val updateNote: UpdateNoteUseCase,
            private val deleteNote: DeleteNoteUseCase,
            private val restoreNote: RestoreNoteUseCase,
            private val clearNote: ClearNoteUseCase,
            private val setNotification: SetNotificationUseCase,
            private val deleteNotification: DeleteNotificationUseCase,
            private val getNotificationDateList: GetNotificationDateListUseCase,
            private val getRankId: GetRankIdUseCase,
            private val getRankDialogNames: GetRankDialogNamesUseCase
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(TextNoteViewModel::class) {
                    val parentCallback = fragment.context as? INoteConnector
                    TextNoteViewModel(
                        fragment, parentCallback, colorConverter, preferencesRepo,
                        interactor, updateNote, deleteNote, restoreNote, clearNote,
                        setNotification, deleteNotification, getNotificationDateList, getRankId,
                        getRankDialogNames
                    )
                }
            }
        }

        class RollNote(
            private val fragment: RollNoteFragment,
            private val colorConverter: ColorConverter,
            private val preferencesRepo: PreferencesRepo,
            private val interactor: IRollNoteInteractor,
            private val updateNote: UpdateNoteUseCase,
            private val deleteNote: DeleteNoteUseCase,
            private val restoreNote: RestoreNoteUseCase,
            private val clearNote: ClearNoteUseCase,
            private val updateVisible: UpdateRollVisibleUseCase,
            private val updateCheck: UpdateRollCheckUseCase,
            private val setNotification: SetNotificationUseCase,
            private val deleteNotification: DeleteNotificationUseCase,
            private val getNotificationDateList: GetNotificationDateListUseCase,
            private val getRankId: GetRankIdUseCase,
            private val getRankDialogNames: GetRankDialogNamesUseCase,
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(RollNoteViewModel::class) {
                    val parentCallback = fragment.context as? INoteConnector
                    RollNoteViewModel(
                        fragment, parentCallback, colorConverter, preferencesRepo,
                        interactor, updateNote, deleteNote, restoreNote, clearNote,
                        updateVisible, updateCheck, setNotification, deleteNotification,
                        getNotificationDateList, getRankId, getRankDialogNames
                    )
                }
            }
        }
    }

    class Alarm(
        private val noteId: Long,
        private val preferencesRepo: PreferencesRepo,
        private val noteRepo: NoteRepo,
        private val getMelodyList: GetMelodyListUseCase,
        private val setNotification: SetNotificationUseCase,
        private val deleteNotification: DeleteNotificationUseCase,
        private val shiftDateIfExist: ShiftDateIfExistUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create(AlarmViewModelImpl::class) {
                AlarmViewModelImpl(
                    noteId, preferencesRepo, noteRepo, getMelodyList,
                    setNotification, deleteNotification, shiftDateIfExist
                )
            }
        }
    }

    class Notification(
        private val setNotification: SetNotificationUseCase,
        private val deleteNotification: DeleteNotificationUseCase,
        private val getList: GetNotificationListUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create(NotificationsViewModelImpl::class) {
                NotificationsViewModelImpl(setNotification, deleteNotification, getList)
            }
        }
    }

    object Preference {

        class Main(
            private val preferencesRepo: PreferencesRepo,
            private val getSummary: GetSummaryUseCase
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(MenuPreferenceViewModelImpl::class) {
                    MenuPreferenceViewModelImpl(preferencesRepo, getSummary)
                }
            }
        }

        class Backup(
            private val permissionResult: PermissionResult?,
            private val getBackupFileList: GetBackupFileListUseCase,
            private val startBackupExport: StartBackupExportUseCase,
            private val startBackupImport: StartBackupImportUseCase
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(BackupPreferenceViewModelImpl::class) {
                    BackupPreferenceViewModelImpl(
                        permissionResult, getBackupFileList, startBackupExport, startBackupImport
                    )
                }
            }
        }

        class Note(
            private val preferencesRepo: PreferencesRepo,
            private val getSortSummary: GetSummaryUseCase,
            private val getDefaultColorSummary: GetSummaryUseCase,
            private val getSavePeriodSummary: GetSummaryUseCase
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(NotePreferenceViewModelImpl::class) {
                    NotePreferenceViewModelImpl(
                        preferencesRepo,
                        getSortSummary, getDefaultColorSummary, getSavePeriodSummary
                    )
                }
            }
        }

        class Alarm(
            private val preferencesRepo: PreferencesRepo,
            private val getSignalSummary: GetSignalSummaryUseCase,
            private val getRepeatSummary: GetSummaryUseCase,
            private val getVolumeSummary: GetSummaryUseCase,
            private val getMelodyList: GetMelodyListUseCase
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(AlarmPreferenceViewModelImpl::class) {
                    AlarmPreferenceViewModelImpl(
                        preferencesRepo, getSignalSummary, getRepeatSummary, getVolumeSummary,
                        getMelodyList
                    )
                }
            }
        }
    }

    object Develop {

        class Main(private val interactor: DevelopInteractor) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(DevelopViewModelImpl::class) {
                    DevelopViewModelImpl(interactor)
                }
            }
        }

        class Print(
            private val type: PrintType,
            private val interactor: DevelopInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(PrintDevelopViewModelImpl::class) {
                    PrintDevelopViewModelImpl(type, interactor)
                }
            }
        }

        class Service : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(ServiceDevelopViewModelImpl::class) {
                    ServiceDevelopViewModelImpl()
                }
            }
        }
    }
}