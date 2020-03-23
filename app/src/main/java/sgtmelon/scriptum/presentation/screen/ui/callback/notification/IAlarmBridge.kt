package sgtmelon.scriptum.presentation.screen.ui.callback.notification

import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl

import sgtmelon.scriptum.interactor.notification.AlarmInteractor
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity

/**
 * Interface for communication [AlarmInteractor] with [AlarmActivity]
 */
interface IAlarmBridge : AlarmControl.Bridge.Set, BindControl.InfoBridge