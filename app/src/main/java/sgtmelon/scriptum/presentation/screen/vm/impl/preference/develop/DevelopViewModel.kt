package sgtmelon.scriptum.presentation.screen.vm.impl.preference.develop

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.preference.develop.IDevelopInteractor
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop.IDevelopFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IDevelopViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IDevelopFragment].
 */
class DevelopViewModel(application: Application) : ParentViewModel<IDevelopFragment>(application),
    IDevelopViewModel {

    private lateinit var interactor: IDevelopInteractor

    fun setInteractor(interactor: IDevelopInteractor) {
        this.interactor = interactor
    }

    private var pingJob: Job? = null


    override fun onSetup(bundle: Bundle?) {
        callback?.setupPrints()
        callback?.setupScreens()
        callback?.setupEternal()
        callback?.setupOther()

        onClickEternalRefresh()
    }


    override fun onClickPrint(type: PrintType) {
        callback?.openPrintScreen(type)
    }

    override fun onClickAlarm() {
        viewModelScope.launch {
            val noteId = runBack { interactor.getRandomNoteId() }
            callback?.openAlarmScreen(noteId)
        }
    }

    override fun onClickReset() {
        interactor.resetPreferences()
        callback?.showToast(R.string.pref_toast_develop_clear)
    }

    //region Eternal functions

    override fun onClickEternalRefresh() {
        pingJob = viewModelScope.launch { startEternalPing() }
    }

    @RunPrivate suspend fun startEternalPing() {
        callback?.updateEternalRefreshEnabled(isEnabled = false)
        callback?.updateEternalRunEnabled(isEnabled = false)
        callback?.updateEternalKillEnabled(isEnabled = false)

        callback?.startEternalPingSummary()

        runBack {
            delay(PING_START_DELAY)
            repeat(PING_REPEAT) {
                callback?.sendEternalPingBroadcast()
                delay(PING_TIMEOUT)
            }
        }

        callback?.stopEternalPingSummary()
        callback?.resetEternalRefreshSummary()

        callback?.updateEternalRefreshEnabled(isEnabled = true)
        callback?.updateEternalRunEnabled(isEnabled = true)
        callback?.updateEternalKillEnabled(isEnabled = false)
    }

    override fun onReceiveEternalServicePong() {
        viewModelScope.launch { pingJob?.cancelAndJoin() }

        callback?.stopEternalPingSummary()
        callback?.resetEternalRefreshSummary()

        callback?.updateEternalRefreshEnabled(isEnabled = true)
        callback?.updateEternalRunEnabled(isEnabled = false)
        callback?.updateEternalKillEnabled(isEnabled = true)
    }

    override fun onClickEternalRun() {
        callback?.runEternalService()
        onClickEternalRefresh()
    }

    override fun onClickEternalKill() {
        callback?.sendEternalKillBroadcast()
        onClickEternalRefresh()
    }

    //endregion

    companion object {
        const val PING_START_DELAY = 3000L
        const val PING_REPEAT = 5
        const val PING_TIMEOUT = 1000L
    }
}