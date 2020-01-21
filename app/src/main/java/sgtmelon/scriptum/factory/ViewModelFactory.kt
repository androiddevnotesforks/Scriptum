package sgtmelon.scriptum.factory

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import sgtmelon.scriptum.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.screen.ui.callback.IDevelopActivity
import sgtmelon.scriptum.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.screen.ui.callback.main.IBinFragment
import sgtmelon.scriptum.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import sgtmelon.scriptum.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.AppViewModel
import sgtmelon.scriptum.screen.vm.DevelopViewModel
import sgtmelon.scriptum.screen.vm.IntroViewModel
import sgtmelon.scriptum.screen.vm.SplashViewModel
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.screen.vm.callback.IDevelopViewModel
import sgtmelon.scriptum.screen.vm.callback.IIntroViewModel
import sgtmelon.scriptum.screen.vm.callback.ISplashViewModel
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
 * Factory for create viewModel
 */
object ViewModelFactory {

    // TODO #RELEASE2 refactor names and structure

    // ---------------------------

    fun getAppViewModel(activity: AppCompatActivity): IAppViewModel {
        return ViewModelProvider(activity).get(AppViewModel::class.java).apply {
            setCallback(activity as? IAppActivity)
        }
    }

    fun getSplashViewModel(activity: AppCompatActivity): ISplashViewModel {
        return ViewModelProvider(activity).get(SplashViewModel::class.java).apply {
            setCallback(activity as? ISplashActivity)
        }
    }

    fun getIntroViewModel(activity: AppCompatActivity): IIntroViewModel {
        return ViewModelProvider(activity).get(IntroViewModel::class.java).apply {
            setCallback(activity as? IIntroActivity)
        }
    }

    // ---------------------------

    fun getNoteViewModel(activity: AppCompatActivity): INoteViewModel {
        return ViewModelProvider(activity).get(NoteViewModel::class.java).apply {
            setCallback(activity as? INoteActivity)
        }
    }

    fun getTextNoteViewModel(fragment: Fragment): ITextNoteViewModel {
        return ViewModelProvider(fragment).get(TextNoteViewModel::class.java).apply {
            setCallback(fragment as? TextNoteFragment)
            setParentCallback(fragment.context as? INoteChild)
        }
    }

    fun getRollNoteViewModel(fragment: Fragment): IRollNoteViewModel {
        return ViewModelProvider(fragment).get(RollNoteViewModel::class.java).apply {
            setCallback(fragment as? RollNoteFragment)
            setParentCallback(fragment.context as? INoteChild)
        }
    }

    // ---------------------------

    fun getMainViewModel(activity: AppCompatActivity): IMainViewModel {
        return ViewModelProvider(activity).get(MainViewModel::class.java).apply {
            setCallback(activity as? IMainActivity)
        }
    }

    fun getRankViewModel(fragment: Fragment): IRankViewModel {
        return ViewModelProvider(fragment).get(RankViewModel::class.java).apply {
            setCallback(fragment as? IRankFragment)
        }
    }

    fun getNotesViewModel(fragment: Fragment): INotesViewModel {
        return ViewModelProvider(fragment).get(NotesViewModel::class.java).apply {
            setCallback(fragment as? INotesFragment)
        }
    }

    fun getBinViewModel(fragment: Fragment): IBinViewModel {
        return ViewModelProvider(fragment).get(BinViewModel::class.java).apply {
            setCallback(fragment as? IBinFragment)
        }
    }

    // ---------------------------

    fun getNotificationViewModel(activity: AppCompatActivity): INotificationViewModel {
        return ViewModelProvider(activity).get(NotificationViewModel::class.java).apply {
            setCallback(activity as? INotificationActivity)
        }
    }

    fun getAlarmViewModel(activity: AppCompatActivity): IAlarmViewModel {
        return ViewModelProvider(activity).get(AlarmViewModel::class.java).apply {
            setCallback(activity as? IAlarmActivity)
        }
    }

    // ---------------------------

    fun getDevelopViewModel(activity: AppCompatActivity): IDevelopViewModel {
        return ViewModelProvider(activity).get(DevelopViewModel::class.java).apply {
            setCallback(activity as? IDevelopActivity)
        }
    }

}