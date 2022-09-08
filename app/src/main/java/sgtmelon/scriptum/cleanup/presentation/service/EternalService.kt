package sgtmelon.scriptum.cleanup.presentation.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import java.util.Calendar
import sgtmelon.common.utils.getCalendar
import sgtmelon.scriptum.cleanup.domain.model.data.ReceiverData
import sgtmelon.scriptum.cleanup.extension.getAlarmService
import sgtmelon.scriptum.cleanup.extension.initLazy
import sgtmelon.scriptum.cleanup.presentation.control.broadcast.BroadcastControl
import sgtmelon.scriptum.cleanup.presentation.receiver.EternalReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.system.ISystemLogic
import sgtmelon.scriptum.cleanup.presentation.screen.system.SystemLogic
import sgtmelon.scriptum.cleanup.presentation.factory.NotificationFactory as Factory

/**
 * [Service] that never will die.
 */
class EternalService : Service(),
    EternalReceiver.Callback {

    private val systemLogic: ISystemLogic = SystemLogic()

    private val receiver by lazy { EternalReceiver[this] }

    private val broadcastControl by lazy { BroadcastControl(context = this) }

    //region System

    override fun onBind(intent: Intent?): IBinder? = null

    /**
     * By returning [Service.START_STICKY] we make sure the service is restarted if the
     * system kills the service.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onCreate() {
        super.onCreate()

        /**
         * Attach this service to notification, which provide long life for them.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Factory.Service.createChannel(context = this)
        }
        startForeground(Factory.Service.ID, Factory.Service[this])

        systemLogic.onCreate(context = this)
        registerReceiver(receiver, IntentFilter(ReceiverData.Filter.ETERNAL))

        broadcastControl.initLazy()
    }

    override fun onDestroy() {
        /**
         * Need call before "super".
         */
        restartService()

        super.onDestroy()

        systemLogic.onDestroy(context = this)
        unregisterReceiver(receiver)
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
        val calendar = getCalendar()
        calendar.add(Calendar.SECOND, 5)

        val service = applicationContext.getAlarmService()
        service?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    //endregion

    override fun killService() = stopSelf()

    override fun sendEternalPongBroadcast() = broadcastControl.sendEternalPong()

    companion object {
        /**
         * Start this foreground service only on API which has channel notifications
         * (for disable it in settings). It means API >= Oreo (26).
         */
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