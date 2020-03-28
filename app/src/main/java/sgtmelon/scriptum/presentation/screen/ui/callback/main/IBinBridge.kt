package sgtmelon.scriptum.presentation.screen.ui.callback.main

import sgtmelon.scriptum.domain.interactor.main.BinInteractor
import sgtmelon.scriptum.presentation.control.system.ClipboardControl
import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment

/**
 * Interface for communication [BinInteractor] with [BinFragment]
 */
interface IBinBridge : ClipboardControl.Bridge