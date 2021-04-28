package sgtmelon.scriptum.presentation.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import sgtmelon.extension.getNewCalendar
import sgtmelon.scriptum.domain.model.data.ReceiverData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.getAlarmService
import sgtmelon.scriptum.extension.initLazy
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.receiver.EternalReceiver
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.service.presenter.IEternalPresenter
import java.util.*
import javax.inject.Inject
import sgtmelon.scriptum.presentation.factory.NotificationFactory as Factory

/**
 * [Service] that never will die.
 */
class EternalService : Service(), IEternalService {

    @Inject internal lateinit var presenter: IEternalPresenter

    private val alarmControl by lazy { AlarmControl[this] }
    private val bindControl by lazy { BindControl[this] }

    private val eternalReceiver by lazy { EternalReceiver[presenter] }

    override fun onBind(intent: Intent?): IBinder? = null

    /**
     * By returning [Service.START_STICKY] we make sure the service is restarted if the
     * system kills the service.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onCreate() {
        super.onCreate()

        ScriptumApplication.component.getEternalBuilder().set(service = this).build()
            .inject(service = this)

        /**
         * Attach this service to notification, which provide long life for them.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Factory.Service.createChannel(context = this)
        }
        startForeground(Factory.Service.ID, Factory.Service[this])

        registerReceiver(eternalReceiver, IntentFilter(ReceiverData.Filter.ETERNAL))

        alarmControl.initLazy()
        bindControl.initLazy()

        presenter.onSetup()
    }

    override fun onDestroy() {
        /**
         * Need call before "super".
         */
        restartService()
        unregisterReceiver(eternalReceiver)

        super.onDestroy()

        presenter.onDestroy()
    }

    /**
     * Restart our service if it was closed.
     */
    private fun restartService() {
        val intent = Intent(applicationContext, EternalService::class.java)
        intent.setPackage(packageName)

        val pendingIntent = PendingIntent.getService(
            this, Factory.Service.REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT
        )

        /**
         * Fire next [EternalService] after 5 seconds.
         */
        val calendar = getNewCalendar()
        calendar.add(Calendar.SECOND, 5)

        val service = applicationContext.getAlarmService()
        service?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    //region Bridge functions

    override fun setAlarm(id: Long, calendar: Calendar, showToast: Boolean) {
        alarmControl.set(calendar, id, showToast)
    }

    override fun cancelAlarm(id: Long) = alarmControl.cancel(id)

    override fun notifyNotesBind(itemList: List<NoteItem>, rankIdVisibleList: List<Long>) {
        bindControl.notifyNotes(itemList, rankIdVisibleList)
    }

    override fun cancelNoteBind(id: Long) = bindControl.cancelNote(id)

    override fun notifyInfoBind(count: Int) = bindControl.notifyInfo(count)

    //endregion

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, EternalService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}