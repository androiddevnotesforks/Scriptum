package sgtmelon.scriptum.cleanup.ui.logic.preference

import kotlinx.coroutines.runBlocking
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.module.data.DataSourceModule
import sgtmelon.scriptum.cleanup.dagger.module.domain.UseCaseModule
import sgtmelon.scriptum.cleanup.ui.screen.preference.AlarmPreferenceScreen
import sgtmelon.scriptum.parent.ui.model.PreferenceItem
import sgtmelon.scriptum.parent.ui.model.PreferenceItem.Header
import sgtmelon.scriptum.parent.ui.model.PreferenceItem.Summary
import sgtmelon.scriptum.parent.ui.model.PreferenceItem.Switch
import sgtmelon.scriptum.parent.ui.parts.preferences.PreferenceLogic

/**
 * Logic for [AlarmPreferenceScreen].
 */
class AlarmPreferenceLogic : PreferenceLogic() {

    private val summaryDataSource = DataSourceModule().provideSummaryDataSource(context.resources)
    val getMelodyList = UseCaseModule().provideGetMelodyListUseCase(
        DataSourceModule().provideRingtoneDataSource(context)
    )

    override fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf<PreferenceItem>(Header(R.string.pref_header_common))

        val signalSummary = summaryDataSource.getSignal(preferencesRepo.signalTypeCheck)
        list.add(Summary.Text(R.string.pref_title_alarm_signal, signalSummary))

        val repeatSummary = summary.getRepeat(preferencesRepo.repeat)
        list.add(Summary.Text(R.string.pref_title_alarm_repeat, repeatSummary))
        list.add(Header(R.string.pref_header_melody_options))

        val isMelody = preferencesRepo.signalState.isMelody
        val melodyList = runBlocking { getMelodyList() }
        val melodyItem = runBlocking {
            val check = preferencesRepo.getMelodyCheck(melodyList)
            return@runBlocking melodyList[check!!]
        }

        list.add(Summary.Text(R.string.pref_title_alarm_melody, melodyItem.title, isMelody))
        list.add(Summary.Text(
            R.string.pref_title_alarm_volume,
            summary.getVolume(preferencesRepo.volumePercent),
            isMelody
        ))
        list.add(Switch(
            R.string.pref_title_alarm_increase,
            R.string.pref_summary_alarm_increase,
            preferencesRepo.isVolumeIncrease,
            isMelody
        ))

        return list
    }

    fun getMelodyDialogPair(): Pair<Array<String>, Int> {
        val melodyList = runBlocking { getMelodyList() }
        val textArray = melodyList.map { it.title }.toTypedArray()
        val initCheck = runBlocking { preferencesRepo.getMelodyCheck(melodyList) }

        return Pair(textArray, initCheck!!)
    }
}