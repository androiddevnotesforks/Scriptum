package sgtmelon.scriptum.presentation.service.presenter

import sgtmelon.scriptum.presentation.receiver.EternalReceiver

interface IEternalPresenter : IParentPresenter, EternalReceiver.Callback {

    fun onSetup()

}