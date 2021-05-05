package sgtmelon.scriptum.presentation.screen.presenter.system

import sgtmelon.scriptum.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.presentation.screen.presenter.IParentPresenter

interface ISystemPresenter : IParentPresenter, SystemReceiver.Callback {

    fun onSetup()

}