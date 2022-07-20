package sgtmelon.scriptum.ui.logic.preference

import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IAlarmPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.AlarmPreferenceInteractor
import sgtmelon.scriptum.cleanup.presentation.control.system.RingtoneControl
import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.data.item.PreferenceItem.Header
import sgtmelon.scriptum.data.item.PreferenceItem.Summary
import sgtmelon.scriptum.data.item.PreferenceItem.Switch
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.ui.logic.parent.ParentPreferenceLogic
import sgtmelon.scriptum.ui.screen.preference.AlarmPreferenceScreen

/**
 * Logic for [AlarmPreferenceScreen].
 */
class AlarmPreferenceLogic : ParentPreferenceLogic() {

    val alarmInteractor: IAlarmPreferenceInteractor = AlarmPreferenceInteractor(
        provider, preferences, SignalConverter()
    )

    val signalInteractor: ISignalInteractor = SignalInteractor(
        RingtoneControl(context), preferences, SignalConverter()
    )

    override fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf(
            Header(R.string.pref_header_common),
            Summary.Text(
                R.string.pref_title_alarm_repeat,
                provider.getRepeat(preferencesRepo.repeat)
            )
        )

        val signalSummary = alarmInteractor.getSignalSummary(signalInteractor.typeCheck)
        list.add(Summary.Text(R.string.pref_title_alarm_signal, signalSummary!!))

        list.add(Header(R.string.pref_header_melody_options))

        val isMelody = signalInteractor.state!!.isMelody
        val melodyItem = runBlocking {
            val check = signalInteractor.getMelodyCheck()!!
            return@runBlocking signalInteractor.getMelodyList()[check]
        }

        list.add(Summary.Text(R.string.pref_title_alarm_melody, melodyItem.title, isMelody))
        list.add(Summary.Text(
            R.string.pref_title_alarm_volume,
            alarmInteractor.getVolumeSummary(),
            isMelody
        ))
        list.add(Switch(
            R.string.pref_title_alarm_increase,
            R.string.pref_summary_alarm_increase,
            preferences.isVolumeIncrease,
            isMelody
        ))

        return list
    }

    fun getMelodyDialogPair(): Pair<Array<String>, Int> {
        val textArray = runBlocking {
            signalInteractor.getMelodyList().map { it.title }.toTypedArray()
        }
        val initCheck = runBlocking { signalInteractor.getMelodyCheck() }

        return Pair(textArray, initCheck!!)
    }

    /**
     * It can't contains only false values. At least need one true.
     */
    fun getRandomSignal(): BooleanArray {
        val melodyValue = Random.nextBoolean()
        val vibrationValue = Random.nextBoolean()

        if (!melodyValue && !vibrationValue) return getRandomSignal()

        return booleanArrayOf(melodyValue, vibrationValue)
    }
}