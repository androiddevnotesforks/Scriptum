package sgtmelon.scriptum.screen.ui.callback.note.text

import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.note.TextNoteInteractor
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment

/**
 * Interface for communication [TextNoteInteractor] with [TextNoteFragment]
 */
interface ITextNoteBridge : AlarmControl.Bridge.Full, BindControl.NoteBridge.Full