package sgtmelon.scriptum.infrastructure.service

import android.app.Service
import sgtmelon.scriptum.infrastructure.screen.parent.ComponentInject
import sgtmelon.scriptum.infrastructure.screen.parent.ReceiverReception

/**
 * Parent class for services with common functions.
 */
abstract class ParentService : Service(),
    ComponentInject,
    ReceiverReception {

    override fun onCreate() {
        super.onCreate()

        startForeground()
        inject()
        checkInReceivers(context = this)
    }

    abstract fun startForeground()

    abstract fun setup()

    override fun onDestroy() {
        super.onDestroy()

        release()
        checkOutReceivers(context = this)
    }

    abstract fun release()

}