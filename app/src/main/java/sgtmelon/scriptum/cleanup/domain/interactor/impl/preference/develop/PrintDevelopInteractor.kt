package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.develop

import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.preferences.PreferenceProvider
import sgtmelon.scriptum.infrastructure.preferences.IPreferenceRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop.IPrintDevelopInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.FileType
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem.Preference
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.cleanup.presentation.control.file.IFileControl
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IPrintDevelopViewModel

/**
 * Interactor for [IPrintDevelopViewModel].
 */
class PrintDevelopInteractor(
    private val developRepo: IDevelopRepo,
    private val key: PreferenceProvider.Key,
    private val def: PreferenceProvider.Def,
    private val preferenceRepo: IPreferenceRepo,
    private val fileControl: IFileControl
) : ParentInteractor(),
    IPrintDevelopInteractor {

    override suspend fun getList(type: PrintType): List<PrintItem> = when (type) {
        PrintType.NOTE -> developRepo.getPrintNoteList(isBin = false)
        PrintType.BIN -> developRepo.getPrintNoteList(isBin = true)
        PrintType.ROLL -> developRepo.getPrintRollList()
        PrintType.VISIBLE -> developRepo.getPrintVisibleList()
        PrintType.RANK -> developRepo.getPrintRankList()
        PrintType.ALARM -> developRepo.getPrintAlarmList()
        PrintType.KEY -> getPreferenceKeyList()
        PrintType.FILE -> getPreferenceFileList()
    }

    @RunPrivate fun getPreferenceKeyList(): List<Preference> {
        return listOf(
            Preference.Title(R.string.pref_header_app),
            Preference.Key(key.firstStart, def.firstStart, preferenceRepo.firstStart),
            Preference.Key(key.theme, def.theme, preferenceRepo.theme),
            Preference.Title(R.string.pref_title_backup),
            Preference.Key(key.importSkip, def.importSkip, preferenceRepo.importSkip),
            Preference.Title(R.string.pref_title_note),
            Preference.Key(key.sort, def.sort, preferenceRepo.sort),
            Preference.Key(key.defaultColor, def.defaultColor, preferenceRepo.defaultColor),
            Preference.Key(key.pauseSaveOn, def.pauseSaveOn, preferenceRepo.pauseSaveOn),
            Preference.Key(key.autoSaveOn, def.autoSaveOn, preferenceRepo.autoSaveOn),
            Preference.Key(key.savePeriod, def.savePeriod, preferenceRepo.savePeriod),
            Preference.Title(R.string.pref_title_alarm),
            Preference.Key(key.repeat, def.repeat, preferenceRepo.repeat),
            Preference.Key(key.signal, def.signal, preferenceRepo.signal),
            Preference.Key(key.melodyUri, def.melodyUri, preferenceRepo.melodyUri),
            Preference.Key(key.volume, def.volume, preferenceRepo.volume),
            Preference.Key(key.volumeIncrease, def.volumeIncrease, preferenceRepo.volumeIncrease),
            Preference.Title(R.string.pref_header_other),
            Preference.Key(key.isDeveloper, def.isDeveloper, preferenceRepo.isDeveloper)
        )
    }

    @RunPrivate suspend fun getPreferenceFileList(): List<Preference> {
        val list = mutableListOf(
            Preference.Title(R.string.pref_header_path_save),
            Preference.Path(fileControl.saveDirectory)
        )

        list.add(Preference.Title(R.string.pref_header_path_files))
        for (it in fileControl.getExternalFiles()) {
            list.add(Preference.Path(it))
        }

        list.add(Preference.Title(R.string.pref_header_path_cache))
        for (it in fileControl.getExternalCache()) {
            list.add(Preference.Path(it))
        }

        list.add(Preference.Title(R.string.pref_header_backup_files))
        for (it in fileControl.getFileList(FileType.BACKUP)) {
            list.add(Preference.File(it))
        }

        return list
    }

}