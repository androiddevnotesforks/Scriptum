package sgtmelon.scriptum.presentation.service.presenter

import sgtmelon.scriptum.presentation.receiver.eternal.BindEternalReceiver

interface IEternalPresenter : IParentPresenter, BindEternalReceiver.Callback {

    fun onSetup()

}