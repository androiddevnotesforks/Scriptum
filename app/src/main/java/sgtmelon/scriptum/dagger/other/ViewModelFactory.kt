package sgtmelon.scriptum.dagger.other

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
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
import kotlin.reflect.KClass

/**
 * ViewModel factory for create ViewModels with constructor parameters.
 */
@Suppress("UNCHECKED_CAST")
object ViewModelFactory {

    // region Help func

    private fun onNotFound() = IllegalArgumentException("ViewModel Not Found")

    private fun <T> Class<T>.create(createFunc: () -> Any): T {
        return if (isAssignableFrom(createFunc::class.java)) createFunc() as T else throw onNotFound()
    }

    // TODO check if upper func work - remove it
    private fun <T> Class<T>.create(modelClass: KClass<*>, createFunc: () -> Any): T {
        return if (isAssignableFrom(modelClass.java)) createFunc() as T else throw onNotFound()
    }

    //endregion

    class Splash(
        private val activity: SplashActivity,
        private val interactor: ISplashInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create { SplashViewModel(activity, interactor) }
        }
    }

    class Intro(
        private val activity: IntroActivity,
        private val interactor: IIntroInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create { IntroViewModel(activity, interactor) }
        }
    }

    object MainScreen {

        class Main(
            private val activity: MainActivity
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create { MainViewModel(activity) }
            }
        }

        class Rank(
            private val fragment: RankFragment,
            private val interactor: IRankInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create { RankViewModel(fragment, interactor) }
            }
        }

        class Notes(
            private val fragment: NotesFragment,
            private val interactor: INotesInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create { NotesViewModel(fragment, interactor) }
            }
        }

        class Bin(
            private val fragment: BinFragment,
            private val interactor: IBinInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create { BinViewModel(fragment, interactor) }
            }
        }
    }

    object NoteScreen {

        class Note(
            private val activity: NoteActivity,
            private val interactor: INoteInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create { NoteViewModel(activity, interactor) }
            }
        }

        class TextNote(
            private val fragment: TextNoteFragment,
            private val interactor: ITextNoteInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create {
                    val parentCallback = fragment.context as? INoteConnector
                    TextNoteViewModel(fragment, parentCallback, interactor)
                }
            }
        }

        class RollNote(
            private val fragment: RollNoteFragment,
            private val interactor: IRollNoteInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create {
                    val parentCallback = fragment.context as? INoteConnector
                    RollNoteViewModel(fragment, parentCallback, interactor)
                }
            }
        }
    }

    class Alarm(
        private val activity: AlarmActivity,
        private val interactor: IAlarmInteractor,
        private val signalInteractor: ISignalInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create { AlarmViewModel(activity, interactor, signalInteractor) }
        }
    }

    class Notification(
        private val activity: NotificationActivity,
        private val interactor: INotificationInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.create { NotificationViewModel(activity, interactor) }
        }
    }

    object Preference {

        class Main(
            private val fragment: PreferenceFragment,
            private val interactor: IPreferenceInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create { PreferenceViewModel(fragment, interactor) }
            }
        }

        class Backup(
            private val fragment: BackupPreferenceFragment,
            private val interactor: IBackupPreferenceInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create { BackupPreferenceViewModel(fragment, interactor) }
            }
        }

        class Note(
            private val fragment: NotePreferenceFragment,
            private val interactor: INotePreferenceInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create { NotePreferenceViewModel(fragment, interactor) }
            }
        }

        class Alarm(
            private val fragment: AlarmPreferenceFragment,
            private val interactor: IAlarmPreferenceInteractor,
            private val signalInteractor: ISignalInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create {
                    AlarmPreferenceViewModel(fragment, interactor, signalInteractor)
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
                return modelClass.create { DevelopViewModel(fragment, interactor) }
            }
        }

        class Print(
            private val activity: PrintDevelopActivity,
            private val interactor: IPrintDevelopInteractor
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create { PrintDevelopViewModel(activity, interactor) }
            }
        }

        class Service(
            private val fragment: ServiceDevelopFragment
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.create { ServiceDevelopViewModel(fragment) }
            }
        }
    }
}