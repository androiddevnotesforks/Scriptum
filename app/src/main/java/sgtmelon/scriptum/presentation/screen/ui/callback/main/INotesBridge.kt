package sgtmelon.scriptum.presentation.screen.ui.callback.main

import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.control.clipboard.ClipboardControl
import sgtmelon.scriptum.interactor.main.NotesInteractor
import sgtmelon.scriptum.presentation.screen.ui.main.NotesFragment

/**
 * Interface for communication [NotesInteractor] with [NotesFragment]
 */
interface INotesBridge : AlarmControl.Bridge.Full,
        BindControl.NoteBridge.Full,
        BindControl.InfoBridge,
        ClipboardControl.Bridge