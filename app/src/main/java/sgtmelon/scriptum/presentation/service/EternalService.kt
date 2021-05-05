package sgtmelon.scriptum.presentation.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import sgtmelon.extension.getNewCalendar
import sgtmelon.scriptum.extension.getAlarmService
import sgtmelon.scriptum.presentation.screen.system.ISystemLogic
import sgtmelon.scriptum.presentation.screen.system.SystemLogic
import java.util.*
import sgtmelon.scriptum.presentation.factory.NotificationFactory as Factory

/**
 * [Service] that never will die.
 */
@RequiresApi(Build.VERSION_CODES.O)
class EternalService : Service() {

    private val systemLogic: ISystemLogic = SystemLogic()

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
        Factory.Service.createChannel(context = this)
        startForeground(Factory.Service.ID, Factory.Service[this])

        systemLogic.onCreate(context = this)
    }

    override fun onDestroy() {
        /**
         * Need call before "super".
         */
        restartService()

        super.onDestroy()

        systemLogic.onDestroy(context = this)
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

    companion object {
        /**
         * Start this foreground service only on API which has channel notifications
         * (for disable it in settings). It means API >= Oreo (26).
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun start(context: Context) {
            context.startForegroundService(Intent(context, EternalService::class.java))
        }
    }
}