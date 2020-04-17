package sgtmelon.scriptum.presentation.screen.ui.callback.note.text

import sgtmelon.scriptum.domain.interactor.impl.note.TextNoteInteractor
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment

/**
 * Interface for communication [TextNoteInteractor] with [TextNoteFragment]
 */
interface ITextNoteBridge : AlarmControl.Bridge.Full,
        BindControl.NoteBridge.Full,
        BindControl.InfoBridge