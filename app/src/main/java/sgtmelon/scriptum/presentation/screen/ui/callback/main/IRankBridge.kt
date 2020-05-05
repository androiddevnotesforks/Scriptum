package sgtmelon.scriptum.presentation.screen.ui.callback.main

import sgtmelon.scriptum.domain.interactor.impl.main.RankInteractor
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment

/**
 * Interface for communication [RankInteractor] with [RankFragment].
 */
interface IRankBridge : BindControl.NoteBridge.NotifyAll