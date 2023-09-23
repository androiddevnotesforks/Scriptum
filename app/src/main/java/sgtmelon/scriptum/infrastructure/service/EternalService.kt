package sgtmelon.scriptum.infrastructure.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import sgtmelon.extensions.getAlarmService
import sgtmelon.extensions.getCalendar
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.infrastructure.model.annotation.notifications.NotificationId
import sgtmelon.scriptum.infrastructure.model.key.ReceiverFilter
import sgtmelon.scriptum.infrastructure.receiver.service.AppSystemReceiver
import sgtmelon.scriptum.infrastructure.receiver.service.ServiceReceiver
import sgtmelon.scriptum.infrastructure.system.delegators.BroadcastDelegator
import java.util.Calendar
import javax.inject.Inject
import sgtmelon.scriptum.cleanup.presentation.factory.NotificationFactory as Factory

/**
 * [Service] that will never die. That needed for work with notifications, alarms, ect.
 * Setup and update system staff outside of UI/Controller classes.
 */
class EternalService : ParentService(),
    ServiceReceiver.Callback {

    @Inject lateinit var logic: EternalServiceLogic

    private val appSystemReceiver by lazy { AppSystemReceiver[logic] }
    private val serviceReceiver = ServiceReceiver[this]
    override val receiverFilter = ReceiverFilter.ETERNAL
    override val receiverList get() = listOf(appSystemReceiver, serviceReceiver)

    private val broadcast = BroadcastDelegator(context = this)

    override fun onBind(intent: Intent?): IBinder? = null

    /**
     * By returning [Service.START_STICKY] we make sure the service is restarted if the
     * system kills the service.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    /** Attach this service to notification, which provide long life for them. */
    override fun startForeground() {
        Factory.Service.createChannel(context = this)
        startForeground(NotificationId.SERVICE, Factory.Service[this])
    }

    override fun inject(component: ScriptumComponent) = component.inject(service = this)

    override fun setup() = logic.setup()

    override fun onDestroy() {
        /** Need call before "super". */
        restartService()
        super.onDestroy()
    }

    override fun release() = logic.release()

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