package sgtmelon.scriptum.presentation.screen.ui.callback.note.text

import sgtmelon.scriptum.interactor.note.TextNoteInteractor
import sgtmelon.scriptum.presentation.control.alarm.AlarmControl
import sgtmelon.scriptum.presentation.control.bind.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment

/**
 * Interface for communication [TextNoteInteractor] with [TextNoteFragment]
 */
interface ITextNoteBridge : AlarmControl.Bridge.Full,
        BindControl.NoteBridge.Full,
        BindControl.InfoBridge