package sgtmelon.scriptum.domain.interactor.impl.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.provider.PreferenceProvider
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.domain.interactor.callback.preference.IPrintInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.domain.model.item.PrintItem.Preference
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPrintViewModel

/**
 * Interactor for [IPrintViewModel].
 */
class PrintInteractor(
    private val developRepo: IDevelopRepo,
    private val key: PreferenceProvider.Key,
    private val def: PreferenceProvider.Def,
    private val preferenceRepo: IPreferenceRepo
) : ParentInteractor(),
    IPrintInteractor {

    override suspend fun getList(type: PrintType): List<PrintItem> = when (type) {
        PrintType.NOTE -> developRepo.getPrintNoteList(isBin = false)
        PrintType.BIN -> developRepo.getPrintNoteList(isBin = true)
        PrintType.ROLL -> developRepo.getPrintRollList()
        PrintType.VISIBLE -> developRepo.getPrintVisibleList()
        PrintType.RANK -> developRepo.getPrintRankList()
        PrintType.ALARM -> developRepo.getPrintAlarmList()
        PrintType.PREFERENCE -> getPreferenceList()
    }

    // TODO add custom things (save path)
    @RunPrivate fun getPreferenceList(): List<PrintItem.Preference> {
        return listOf(
            Preference.Divider(R.string.pref_header_app),
            Preference.Item(key.firstStart, def.firstStart, preferenceRepo.firstStart),
            Preference.Item(key.theme, def.theme, preferenceRepo.theme),
            Preference.Divider(R.string.pref_header_backup),
            Preference.Item(key.importSkip, def.importSkip, preferenceRepo.importSkip),
            Preference.Divider(R.string.pref_header_note),
            Preference.Item(key.sort, def.sort, preferenceRepo.sort),
            Preference.Item(key.defaultColor, def.defaultColor, preferenceRepo.defaultColor),
            Preference.Item(key.pauseSaveOn, def.pauseSaveOn, preferenceRepo.pauseSaveOn),
            Preference.Item(key.autoSaveOn, def.autoSaveOn, preferenceRepo.autoSaveOn),
            Preference.Item(key.savePeriod, def.savePeriod, preferenceRepo.savePeriod),
            Preference.Divider(R.string.pref_header_alarm),
            Preference.Item(key.repeat, def.repeat, preferenceRepo.repeat),
            Preference.Item(key.signal, def.signal, preferenceRepo.signal),
            Preference.Item(key.melodyUri, def.melodyUri, preferenceRepo.melodyUri),
            Preference.Item(key.volume, def.volume, preferenceRepo.volume),
            Preference.Item(key.volumeIncrease, def.volumeIncrease, preferenceRepo.volumeIncrease),
            Preference.Divider(R.string.pref_header_other),
            Preference.Item(key.isDeveloper, def.isDeveloper, preferenceRepo.isDeveloper),
            Preference.Divider(R.string.pref_header_develop)
        )
    }

}