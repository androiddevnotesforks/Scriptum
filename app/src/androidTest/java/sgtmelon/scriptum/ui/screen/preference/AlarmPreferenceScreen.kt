package sgtmelon.scriptum.ui.screen.preference

import kotlinx.coroutines.runBlocking
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.interactor.callback.preference.IAlarmPreferenceInteractor
import sgtmelon.scriptum.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.domain.interactor.impl.preference.AlarmPreferenceInteractor
import sgtmelon.scriptum.presentation.control.system.RingtoneControl
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.ui.dialog.preference.RepeatDialogUi

/**
 * Class for UI control of [AlarmPreferenceFragment].
 */
class AlarmPreferenceScreen : ParentPreferenceScreen(R.string.pref_title_alarm) {

    private val interactor: IAlarmPreferenceInteractor = AlarmPreferenceInteractor(
        provider, preferenceRepo, IntConverter()
    )

    private val signalInteractor: ISignalInteractor = SignalInteractor(
        RingtoneControl(context), preferenceRepo, IntConverter()
    )

    override fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf(
            PreferenceItem.Header(R.string.pref_header_common),
            PreferenceItem.Summary(R.string.pref_title_alarm_repeat, provider.repeat[preferenceRepo.repeat])
        )

        val signalSummary = interactor.getSignalSummary(signalInteractor.typeCheck)
        list.add(PreferenceItem.Summary(R.string.pref_title_alarm_signal, signalSummary!!))

        list.add(PreferenceItem.Header(R.string.pref_header_melody_options))

        val isMelody = signalInteractor.state!!.isMelody
        val melodyItem = runBlocking { signalInteractor.getMelodyList()[signalInteractor.getMelodyCheck()!!] }

        list.add(PreferenceItem.Summary(R.string.pref_title_alarm_melody, melodyItem.title, isMelody))
        list.add(PreferenceItem.Summary(R.string.pref_title_alarm_volume, interactor.getVolumeSummary(), isMelody))
        list.add(PreferenceItem.Switch(R.string.pref_title_alarm_increase, R.string.pref_summary_alarm_increase, preferenceRepo.volumeIncrease, isMelody))

        return list
    }

    fun openRepeatDialog(func: RepeatDialogUi.() -> Unit = {}) {
        getItem(p = 1).Summary().onItemClick()
        RepeatDialogUi(func)
    }

    companion object {
        operator fun invoke(func: AlarmPreferenceScreen.() -> Unit): AlarmPreferenceScreen {
            return AlarmPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}