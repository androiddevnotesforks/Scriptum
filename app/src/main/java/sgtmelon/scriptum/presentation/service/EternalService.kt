package sgtmelon.scriptum.presentation.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.factory.NotificationFactory as Factory

/**
 * [Service] that never will die.
 */
class EternalService : Service(), IEternalService {

    // TODO
    //    @Inject internal lateinit var presenter: IEternalPresenter

    private val bindControl by lazy { BindControl[this] }

    // TODO
    //    private val bindReceiver by lazy { BindReceiver[presenter] }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        /**
         * Attach this service to notification, which provide long life for them.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Factory.Service.createChannel(context = this)
        }
        startForeground(Factory.Service.ID, Factory.Service[this])

        //        registerReceiver(bindReceiver, IntentFilter(ReceiverData.Filter.BIND))
    }

    /**
     * By returning [Service.START_STICKY] we make sure the service is restarted if the
     * system kills the service.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    /**
     * Restart our service if it was closed.
     */
    override fun onTaskRemoved(rootIntent: Intent?) {
        val intent = Intent(applicationContext, EternalService::class.java)
        intent.setPackage(packageName)

        val pendingIntent = PendingIntent.getService(
            this, Factory.Service.REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT
        )
        val triggerTime = SystemClock.elapsedRealtime() + 2000L
        val service = applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        service?.set(AlarmManager.ELAPSED_REALTIME, triggerTime, pendingIntent)
    }
}