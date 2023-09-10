package sgtmelon.scriptum.infrastructure.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import sgtmelon.extensions.getAlarmService
import sgtmelon.extensions.getCalendar
import sgtmelon.scriptum.infrastructure.model.annotation.notifications.NotificationId
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.receiver.service.AppSystemReceiver
import sgtmelon.scriptum.infrastructure.receiver.service.ServiceReceiver
import sgtmelon.scriptum.infrastructure.screen.ScriptumApplication
import sgtmelon.scriptum.infrastructure.system.delegators.BroadcastDelegator
import java.util.Calendar
import javax.inject.Inject
import sgtmelon.scriptum.cleanup.presentation.factory.NotificationFactory as Factory

/**
 * [Service] that will never die. That needed for work with notifications, alarms, ect.
 * Setup and update system staff outside of UI/Controller classes.
 */
class EternalService : Service(),
    ServiceReceiver.Callback {

    @Inject lateinit var logic: EternalServiceLogic

    private val serviceReceiver = ServiceReceiver[this]
    private val appSystemReceiver by lazy { AppSystemReceiver[logic] }

    private val broadcast = BroadcastDelegator(context = this)

    override fun onBind(intent: Intent?): IBinder? = null

    /**
     * By returning [Service.START_STICKY] we make sure the service is restarted if the
     * system kills the service.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onCreate() {
        super.onCreate()

        /** Attach this service to notification, which provide long life for them. */
        Factory.Service.createChannel(context = this)
        startForeground(NotificationId.SERVICE, Factory.Service[this])

        ScriptumApplication.component.inject(service = this)
        logic.setup()

        registerReceiver(appSystemReceiver, IntentFilter(ReceiverData.Filter.SYSTEM))
        registerReceiver(serviceReceiver, IntentFilter(ReceiverData.Filter.ETERNAL))
    }

    override fun onDestroy() {
        /** Need call before "super". */
        restartService()

        super.onDestroy()

        logic.release()

        unregisterReceiver(appSystemReceiver)
        unregisterReceiver(serviceReceiver)
    }

    /** Restart our service if it was closed. */
    private fun restartService() {
        val intent = Intent(applicationContext, EternalService::class.java)
        intent.setPackage(packageName)

        val pendingIntent = PendingIntent.getService(
            this, NotificationId.SERVICE, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        /** Fire next [EternalService] after 5 seconds. */
        val calendar = getCalendar()
        calendar.add(Calendar.SECOND, RESTART_TIME)

        val service = applicationContext.getAlarmService()
        service.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    override fun killService() = stopSelf()

    override fun sendPongBroadcast() = broadcast.sendEternalPong()

    companion object {

        private const val RESTART_TIME = 5

        fun start(context: Context) {
            context.startForegroundService(Intent(context, EternalService::class.java))
        }
    }
}