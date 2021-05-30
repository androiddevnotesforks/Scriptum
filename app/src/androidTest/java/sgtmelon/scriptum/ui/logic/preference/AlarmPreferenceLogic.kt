package sgtmelon.scriptum.ui.logic.preference

import kotlinx.coroutines.runBlocking
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.item.PreferenceItem
import sgtmelon.scriptum.data.item.PreferenceItem.*
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.interactor.callback.preference.IAlarmPreferenceInteractor
import sgtmelon.scriptum.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.domain.interactor.impl.preference.AlarmPreferenceInteractor
import sgtmelon.scriptum.presentation.control.system.RingtoneControl
import sgtmelon.scriptum.ui.logic.parent.ParentPreferenceLogic
import sgtmelon.scriptum.ui.screen.preference.AlarmPreferenceScreen
import kotlin.random.Random

/**
 * Logic for [AlarmPreferenceScreen].
 */
class AlarmPreferenceLogic : ParentPreferenceLogic() {

    val alarmInteractor: IAlarmPreferenceInteractor = AlarmPreferenceInteractor(
        provider, preferenceRepo, IntConverter()
    )

    val signalInteractor: ISignalInteractor = SignalInteractor(
        RingtoneControl(context), preferenceRepo, IntConverter()
    )

    override fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf(
            Header(R.string.pref_header_common),
            Summary.Text(R.string.pref_title_alarm_repeat, provider.repeat[preferenceRepo.repeat])
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
            preferenceRepo.volumeIncrease,
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