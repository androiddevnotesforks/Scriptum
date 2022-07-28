package sgtmelon.scriptum.cleanup.dagger.other

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IBackupPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop.IDevelopInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop.IPrintDevelopInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note.INoteConnector
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
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.AppViewModel
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
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCase
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter

/**
 * ViewModel factory for create ViewModels with constructor parameters.
 */
@Suppress("UNCHECKED_CAST")
object ViewModelFactory {

    // region Help func

    private fun onNotFound() = IllegalArgumentException("ViewModel Not Found")

    private fun <T> Class<T>.create(modelClass: KClass<*>, createFunc: () -> Any): T {
        return if (isAssignableFrom(modelClass.java)) createFunc() as T else throw onNotFound()
    }

    //endregion

    class App(
        private val activity: IAppActivity,
        private val preferencesRepo: PreferencesRepo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create(AppViewModel::class) {
                AppViewModel(activity, preferencesRepo)
            }
        }
    }

    class Splash(
        private val activity: SplashActivity,
        private val preferencesRepo: PreferencesRepo,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create(SplashViewModel::class) {
                SplashViewModel(activity, preferencesRepo)
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

        class Main(
            private val activity: MainActivity
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(MainViewModel::class) {
                    MainViewModel(activity)
                }
            }
        }

        class Rank(
            private val fragment: RankFragment,
            private val interactor: IRankInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(RankViewModel::class) {
                    RankViewModel(fragment, interactor)
                }
            }
        }

        class Notes(
            private val fragment: NotesFragment,
            private val preferencesRepo: PreferencesRepo,
            private val interactor: INotesInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(NotesViewModel::class) {
                    NotesViewModel(fragment, preferencesRepo, interactor)
                }
            }
        }

        class Bin(
            private val fragment: BinFragment,
            private val interactor: IBinInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(BinViewModel::class) {
                    BinViewModel(fragment, interactor)
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
            private val interactor: ITextNoteInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(TextNoteViewModel::class) {
                    val parentCallback = fragment.context as? INoteConnector
                    TextNoteViewModel(
                        fragment, parentCallback, colorConverter, preferencesRepo, interactor
                    )
                }
            }
        }

        class RollNote(
            private val fragment: RollNoteFragment,
            private val colorConverter: ColorConverter,
            private val preferencesRepo: PreferencesRepo,
            private val interactor: IRollNoteInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(RollNoteViewModel::class) {
                    val parentCallback = fragment.context as? INoteConnector
                    RollNoteViewModel(
                        fragment, parentCallback, colorConverter, preferencesRepo, interactor
                    )
                }
            }
        }
    }

    class Alarm(
        private val activity: AlarmActivity,
        private val preferencesRepo: PreferencesRepo,
        private val interactor: IAlarmInteractor,
        private val signalInteractor: ISignalInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create(AlarmViewModel::class) {
                AlarmViewModel(activity, preferencesRepo, interactor, signalInteractor)
            }
        }
    }

    class Notification(
        private val activity: NotificationActivity,
        private val interactor: INotificationInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create(NotificationViewModel::class) {
                NotificationViewModel(activity, interactor)
            }
        }
    }

    object Preference {

        class Main(
            private val fragment: PreferenceFragment,
            private val preferencesRepo: PreferencesRepo,
            private val getSummary: GetSummaryUseCase
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(PreferenceViewModel::class) {
                    PreferenceViewModel(fragment, preferencesRepo, getSummary)
                }
            }
        }

        class Backup(
            private val fragment: BackupPreferenceFragment,
            private val interactor: IBackupPreferenceInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(BackupPreferenceViewModel::class) {
                    BackupPreferenceViewModel(fragment, interactor)
                }
            }
        }

        class Note(
            private val fragment: NotePreferenceFragment,
            private val preferencesRepo: PreferencesRepo,
            private val getSortSummary: GetSummaryUseCase,
            private val getDefaultColorSummary: GetSummaryUseCase,
            private val getSavePeriodSummary: GetSummaryUseCase
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(NotePreferenceViewModel::class) {
                    NotePreferenceViewModel(
                        fragment, preferencesRepo,
                        getSortSummary, getDefaultColorSummary, getSavePeriodSummary
                    )
                }
            }
        }

        class Alarm(
            private val fragment: AlarmPreferenceFragment,
            private val preferencesRepo: PreferencesRepo,
            private val getRepeatSummary: GetSummaryUseCase,
            private val getVolumeSummary: GetSummaryUseCase,
            private val getSignalSummary: GetSignalSummaryUseCase,
            private val signalInteractor: ISignalInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(AlarmPreferenceViewModel::class) {
                    AlarmPreferenceViewModel(
                        fragment,
                        preferencesRepo, getRepeatSummary, getVolumeSummary, getSignalSummary,
                        signalInteractor
                    )
                }
            }
        }
    }

    object Develop {

        class Main(
            private val fragment: DevelopFragment,
            private val interactor: IDevelopInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(DevelopViewModel::class) {
                    DevelopViewModel(fragment, interactor)
                }
            }
        }

        class Print(
            private val activity: PrintDevelopActivity,
            private val interactor: IPrintDevelopInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(PrintDevelopViewModel::class) {
                    PrintDevelopViewModel(activity, interactor)
                }
            }
        }

        class Service(
            private val fragment: ServiceDevelopFragment
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create(ServiceDevelopViewModel::class) {
                    ServiceDevelopViewModel(fragment)
                }
            }
        }
    }
}