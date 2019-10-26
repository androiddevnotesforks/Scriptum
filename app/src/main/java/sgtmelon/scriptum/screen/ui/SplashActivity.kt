package sgtmelon.scriptum.screen.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.control.bind.IBindControl
import sgtmelon.scriptum.extension.beforeFinish
import sgtmelon.scriptum.extension.hideKeyboard
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.OpenFrom
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import java.util.*

/**
 * Start screen of application
 */
class SplashActivity : AppCompatActivity(), ISplashActivity {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private val iViewModel by lazy { ViewModelFactory.getSplashViewModel(activity = this) }

    private val iAlarmControl by lazy { AlarmControl[this] }
    private val iBindControl: IBindControl by lazy { BindControl(context = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iAlarmControl.initLazy()
        iBindControl.initLazy()

        /**
         * If keyboard was open in another app
         */
        hideKeyboard()

        beforeFinish { iViewModel.onSetup(intent.extras) }
    }

    override fun onDestroy() {
        super.onDestroy()
        iViewModel.onDestroy()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun startIntroActivity() = startActivity(IntroActivity[this])

    override fun startMainActivity() = startActivity(MainActivity[this])

    override fun startAlarmActivity(id: Long) {
        startActivities(arrayOf(MainActivity[this], AlarmActivity[this, id]))
    }

    override fun startNoteActivity(id: Long, type: NoteType) {
        startActivities(arrayOf(MainActivity[this], NoteActivity[this, type, id]))
    }

    override fun startNotificationActivity() {
        startActivities(arrayOf(MainActivity[this], NotificationActivity[this]))
    }


    override fun setAlarm(calendar: Calendar, id: Long) {
        iAlarmControl.set(calendar, id, showToast = false)
    }

    override fun cancelAlarm(id: Long) = iAlarmControl.cancel(id)

    override fun notifyNoteBind(noteModel: NoteModel, rankIdVisibleList: List<Long>) {
        iBindControl.notifyNote(noteModel, rankIdVisibleList)
    }

    override fun notifyInfoBind(count: Int) = iBindControl.notifyInfo(count)

    companion object {
        fun getAlarmInstance(context: Context, noteEntity: NoteEntity) =
                getAlarmInstance(context, noteEntity.id)

        fun getAlarmInstance(context: Context, id: Long): Intent =
                Intent(context, SplashActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(OpenFrom.INTENT_KEY, OpenFrom.ALARM)
                        .putExtra(NoteData.Intent.ID, id)

        fun getBindInstance(context: Context, noteEntity: NoteEntity): Intent =
                Intent(context, SplashActivity::class.java)
                        .putExtra(OpenFrom.INTENT_KEY, OpenFrom.BIND)
                        .putExtra(NoteData.Intent.ID, noteEntity.id)
                        .putExtra(NoteData.Intent.TYPE, noteEntity.type.ordinal)

        fun getNotificationInstance(context: Context): Intent =
                Intent(context, SplashActivity::class.java)
                        .putExtra(OpenFrom.INTENT_KEY, OpenFrom.INFO)
    }

}