package sgtmelon.scriptum.factory

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.DevelopActivity
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.ui.callback.*
import sgtmelon.scriptum.screen.ui.callback.main.IBinFragment
import sgtmelon.scriptum.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.*
import sgtmelon.scriptum.screen.vm.callback.*
import sgtmelon.scriptum.screen.vm.callback.main.IBinViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IMainViewModel
import sgtmelon.scriptum.screen.vm.callback.main.INotesViewModel
import sgtmelon.scriptum.screen.vm.callback.main.IRankViewModel
import sgtmelon.scriptum.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.screen.vm.callback.note.IRollNoteViewModel
import sgtmelon.scriptum.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.screen.vm.callback.notification.INotificationViewModel
import sgtmelon.scriptum.screen.vm.main.BinViewModel
import sgtmelon.scriptum.screen.vm.main.MainViewModel
import sgtmelon.scriptum.screen.vm.main.NotesViewModel
import sgtmelon.scriptum.screen.vm.main.RankViewModel
import sgtmelon.scriptum.screen.vm.note.NoteViewModel
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import sgtmelon.scriptum.screen.vm.notification.NotificationViewModel

/**
 * Factory for create viewModel's.
 */
object ViewModelFactory {

    fun get(activity: AppActivity): IAppViewModel {
        return ViewModelProvider(activity).get(AppViewModel::class.java).apply {
            setCallback(activity as? IAppActivity)
        }
    }

    fun get(activity: SplashActivity): ISplashViewModel {
        return ViewModelProvider(activity).get(SplashViewModel::class.java).apply {
            setCallback(activity as? ISplashActivity)
        }
    }

    fun get(activity: IntroActivity): IIntroViewModel {
        return ViewModelProvider(activity).get(IntroViewModel::class.java).apply {
            setCallback(activity as? IIntroActivity)
        }
    }

    object Note {
        fun get(activity: NoteActivity): INoteViewModel {
            return ViewModelProvider(activity).get(NoteViewModel::class.java).apply {
                setCallback(activity as? INoteActivity)
            }
        }

        fun get(fragment: TextNoteFragment): ITextNoteViewModel {
            return ViewModelProvider(fragment).get(TextNoteViewModel::class.java).apply {
                setCallback(fragment as? TextNoteFragment)
                setParentCallback(fragment.context as? INoteChild)
            }
        }

        fun get(fragment: RollNoteFragment): IRollNoteViewModel {
            return ViewModelProvider(fragment).get(RollNoteViewModel::class.java).apply {
                setCallback(fragment as? RollNoteFragment)
                setParentCallback(fragment.context as? INoteChild)
            }
        }
    }

    object Main {
        fun get(activity: MainActivity): IMainViewModel {
            return ViewModelProvider(activity).get(MainViewModel::class.java).apply {
                setCallback(activity as? IMainActivity)
            }
        }

        fun get(fragment: RankFragment): IRankViewModel {
            return ViewModelProvider(fragment).get(RankViewModel::class.java).apply {
                setCallback(fragment as? IRankFragment)
            }
        }

        fun get(fragment: NotesFragment): INotesViewModel {
            return ViewModelProvider(fragment).get(NotesViewModel::class.java).apply {
                setCallback(fragment as? INotesFragment)
            }
        }

        fun get(fragment: BinFragment): IBinViewModel {
            return ViewModelProvider(fragment).get(BinViewModel::class.java).apply {
                setCallback(fragment as? IBinFragment)
            }
        }
    }

    fun get(activity: NotificationActivity): INotificationViewModel {
        return ViewModelProvider(activity).get(NotificationViewModel::class.java).apply {
            setCallback(activity as? INotificationActivity)
        }
    }

    fun get(activity: AlarmActivity): IAlarmViewModel {
        return ViewModelProvider(activity).get(AlarmViewModel::class.java).apply {
            setCallback(activity as? IAlarmActivity)
        }
    }

    fun get(context: Context, callback: IPreferenceFragment): IPreferenceViewModel {
        return PreferenceViewModel(context, callback)
    }

    fun get(activity: DevelopActivity): IDevelopViewModel {
        return ViewModelProvider(activity).get(DevelopViewModel::class.java).apply {
            setCallback(activity as? IDevelopActivity)
        }
    }

}