package sgtmelon.scriptum.presentation.screen.ui.callback.note.roll

import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.note.RollNoteInteractor
import sgtmelon.scriptum.presentation.screen.ui.note.RollNoteFragment

/**
 * Interface for communication [RollNoteInteractor] with [RollNoteFragment]
 */
interface IRollNoteBridge : AlarmControl.Bridge.Full,
        BindControl.NoteBridge.Full,
        BindControl.InfoBridge