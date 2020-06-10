package sgtmelon.scriptum.presentation.screen.ui.callback.note

import sgtmelon.scriptum.domain.interactor.impl.note.RollNoteInteractor
import sgtmelon.scriptum.domain.interactor.impl.note.TextNoteInteractor
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment

/**
 * Interface for communication [TextNoteInteractor]/[RollNoteInteractor]
 * with [TextNoteFragment]/[RollNoteFragment].
 */
interface IParentNoteBridge : AlarmControl.Bridge.Full,
        BindControl.NoteBridge.Full,
        BindControl.InfoBridge