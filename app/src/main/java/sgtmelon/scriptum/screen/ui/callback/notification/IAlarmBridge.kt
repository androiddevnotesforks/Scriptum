package sgtmelon.scriptum.screen.ui.callback.notification

import sgtmelon.scriptum.control.alarm.AlarmControl

import sgtmelon.scriptum.interactor.notification.AlarmInteractor
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity

/**
 * Interface for communication [AlarmInteractor] with [AlarmActivity]
 */
interface IAlarmBridge : AlarmControl.Bridge.Set