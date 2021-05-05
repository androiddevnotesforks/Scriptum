package sgtmelon.scriptum.presentation.screen.presenter.system

import sgtmelon.scriptum.presentation.receiver.EternalReceiver
import sgtmelon.scriptum.presentation.screen.presenter.IParentPresenter

interface ISystemPresenter : IParentPresenter, EternalReceiver.Callback {

    fun onSetup()

}