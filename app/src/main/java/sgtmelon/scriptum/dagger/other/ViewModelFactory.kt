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
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
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
}