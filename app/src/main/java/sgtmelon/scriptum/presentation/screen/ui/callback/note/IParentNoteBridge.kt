package sgtmelon.scriptum.presentation.screen.ui.callback.note

import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment

/**
 * Interface for communication [ITextNoteInteractor]/[IRollNoteInteractor]
 * with [TextNoteFragment]/[RollNoteFragment].
 */
interface IParentNoteBridge : AlarmControl.Bridge.Full,
        BindControl.NoteBridge.Full,
        BindControl.InfoBridge