package sgtmelon.scriptum.cleanup.presentation.screen.presenter.system

import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.presenter.IParentPresenter

interface ISystemPresenter : IParentPresenter, SystemReceiver.Callback {

    fun onSetup()

}