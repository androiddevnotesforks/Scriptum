package sgtmelon.scriptum.presentation.screen.ui.callback.note.roll

import sgtmelon.scriptum.domain.interactor.note.RollNoteInteractor
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment

/**
 * Interface for communication [RollNoteInteractor] with [RollNoteFragment]
 */
interface IRollNoteBridge : AlarmControl.Bridge.Full,
        BindControl.NoteBridge.Full,
        BindControl.InfoBridge