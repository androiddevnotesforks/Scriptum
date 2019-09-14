package sgtmelon.scriptum.factory

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.screen.ui.callback.IDevelopActivity
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
import sgtmelon.scriptum.screen.vm.SplashViewModel
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.screen.vm.callback.IDevelopViewModel
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

    fun getAppViewModel(activity: AppCompatActivity): IAppViewModel =
            ViewModelProviders.of(activity).get(AppViewModel::class.java).apply {
                callback = activity as? IAppActivity
            }

    fun getSplashViewModel(activity: AppCompatActivity): ISplashViewModel =
            ViewModelProviders.of(activity).get(SplashViewModel::class.java).apply {
                callback = activity as? ISplashActivity
            }

    // ---------------------------

    fun getNoteViewModel(activity: AppCompatActivity): INoteViewModel =
            ViewModelProviders.of(activity).get(NoteViewModel::class.java).apply {
                callback = activity as? INoteActivity
            }

    fun getTextNoteViewModel(fragment: Fragment): ITextNoteViewModel =
            ViewModelProviders.of(fragment).get(TextNoteViewModel::class.java).apply {
                callback = fragment as? TextNoteFragment
                parentCallback = fragment.context as? INoteChild
            }

    fun getRollNoteViewModel(fragment: Fragment): IRollNoteViewModel =
            ViewModelProviders.of(fragment).get(RollNoteViewModel::class.java).apply {
                callback = fragment as? RollNoteFragment
                parentCallback = fragment.context as? INoteChild
            }

    // ---------------------------

    fun getMainViewModel(activity: AppCompatActivity): IMainViewModel =
            ViewModelProviders.of(activity).get(MainViewModel::class.java).apply {
                callback = activity as? IMainActivity
            }

    fun getRankViewModel(fragment: Fragment): IRankViewModel =
            ViewModelProviders.of(fragment).get(RankViewModel::class.java).apply {
                callback = fragment as? IRankFragment
            }

    fun getNotesViewModel(fragment: Fragment): INotesViewModel =
            ViewModelProviders.of(fragment).get(NotesViewModel::class.java).apply {
                callback = fragment as? INotesFragment
            }

    fun getBinViewModel(fragment: Fragment): IBinViewModel =
            ViewModelProviders.of(fragment).get(BinViewModel::class.java).apply {
                callback = fragment as? IBinFragment
            }

    // ---------------------------

    fun getNotificationViewModel(activity: AppCompatActivity): INotificationViewModel =
            ViewModelProviders.of(activity).get(NotificationViewModel::class.java).apply {
                callback = activity as? INotificationActivity
            }

    fun getAlarmViewModel(activity: AppCompatActivity): IAlarmViewModel =
            ViewModelProviders.of(activity).get(AlarmViewModel::class.java).apply {
                callback = activity as? IAlarmActivity
            }

    // ---------------------------

    fun getDevelopViewModel(activity: AppCompatActivity): IDevelopViewModel =
            ViewModelProviders.of(activity).get(DevelopViewModel::class.java).apply {
                callback = activity as? IDevelopActivity
            }

}