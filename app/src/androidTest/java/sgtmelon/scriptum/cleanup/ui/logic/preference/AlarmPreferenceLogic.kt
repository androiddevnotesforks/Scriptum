package sgtmelon.scriptum.cleanup.ui.logic.preference

import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.module.base.data.DataSourceModule
import sgtmelon.scriptum.cleanup.dagger.module.base.domain.UseCaseModule
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem.Header
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem.Summary
import sgtmelon.scriptum.cleanup.testData.item.PreferenceItem.Switch
import sgtmelon.scriptum.cleanup.ui.logic.parent.ParentPreferenceLogic
import sgtmelon.scriptum.cleanup.ui.screen.preference.AlarmPreferenceScreen

/**
 * Logic for [AlarmPreferenceScreen].
 */
class AlarmPreferenceLogic : ParentPreferenceLogic() {

    private val summaryDataSource = DataSourceModule().provideSummaryDataSource(context.resources)
    val getMelodyList = UseCaseModule().provideGetMelodyListUseCase(
        DataSourceModule().provideRingtoneDataSource(context)
    )

    override fun getScreenList(): List<PreferenceItem> {
        val list = mutableListOf(
            Header(R.string.pref_header_common),
            Summary.Text(
                R.string.pref_title_alarm_repeat,
                provider.getRepeat(preferencesRepo.repeat)
            )
        )

        val signalSummary = summaryDataSource.getSignal(preferencesRepo.signalTypeCheck)
        list.add(Summary.Text(R.string.pref_title_alarm_signal, signalSummary))

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
            provider.getVolume(preferencesRepo.volume),
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
        val melodyList = runBlocking { getMelodyList() }
        val textArray = melodyList.map { it.title }.toTypedArray()
        val initCheck = runBlocking { preferencesRepo.getMelodyCheck(melodyList) }

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