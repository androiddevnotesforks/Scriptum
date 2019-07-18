package sgtmelon.scriptum.screen.callback.notification

import android.os.Bundle
import sgtmelon.scriptum.screen.view.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

/**
 * Интерфейс для общения [AlarmActivity] с [AlarmViewModel]
 *
 * @author SerjantArbuz
 */
interface IAlarmViewModel {

    fun onSetup()

    fun onSetupData(bundle: Bundle?)

    fun onStart()

    fun onDestroy()

    fun onSaveData(bundle: Bundle)

    fun onClickNote()

    fun onClickDisable()

    fun onClickPostpone()

}