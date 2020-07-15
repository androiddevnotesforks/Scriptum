package sgtmelon.scriptum.presentation.screen.ui.callback.main

import sgtmelon.scriptum.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.control.system.ClipboardControl
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment

/**
 * Interface for communication [INotesInteractor] with [NotesFragment].
 */
interface INotesBridge : AlarmControl.Bridge.Full,
        BindControl.NoteBridge.Full,
        BindControl.InfoBridge,
        ClipboardControl.Bridge