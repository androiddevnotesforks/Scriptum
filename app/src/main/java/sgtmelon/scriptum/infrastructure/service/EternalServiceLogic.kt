package sgtmelon.scriptum.infrastructure.service

import sgtmelon.scriptum.infrastructure.receiver.service.AppSystemReceiver

interface EternalServiceLogic : AppSystemReceiver.Callback {

    fun setup()

    fun release()

}