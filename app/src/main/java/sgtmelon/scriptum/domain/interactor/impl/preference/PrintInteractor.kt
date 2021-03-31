package sgtmelon.scriptum.domain.interactor.impl.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.provider.PreferenceProvider
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.domain.interactor.callback.preference.IPrintInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.FileType
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.domain.model.item.PrintItem.Preference
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.presentation.control.file.IFileControl
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPrintViewModel

/**
 * Interactor for [IPrintViewModel].
 */
class PrintInteractor(
    private val developRepo: IDevelopRepo,
    private val key: PreferenceProvider.Key,
    private val def: PreferenceProvider.Def,
    private val preferenceRepo: IPreferenceRepo,
    private val fileControl: IFileControl
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
    @RunPrivate suspend fun getPreferenceList(): List<Preference> {
        val list = mutableListOf(
            Preference.Title(R.string.pref_header_app),
            Preference.Item(key.firstStart, def.firstStart, preferenceRepo.firstStart),
            Preference.Item(key.theme, def.theme, preferenceRepo.theme),
            Preference.Title(R.string.pref_header_backup),
            Preference.Item(key.importSkip, def.importSkip, preferenceRepo.importSkip),
            Preference.Title(R.string.pref_header_note),
            Preference.Item(key.sort, def.sort, preferenceRepo.sort),
            Preference.Item(key.defaultColor, def.defaultColor, preferenceRepo.defaultColor),
            Preference.Item(key.pauseSaveOn, def.pauseSaveOn, preferenceRepo.pauseSaveOn),
            Preference.Item(key.autoSaveOn, def.autoSaveOn, preferenceRepo.autoSaveOn),
            Preference.Item(key.savePeriod, def.savePeriod, preferenceRepo.savePeriod),
            Preference.Title(R.string.pref_header_alarm),
            Preference.Item(key.repeat, def.repeat, preferenceRepo.repeat),
            Preference.Item(key.signal, def.signal, preferenceRepo.signal),
            Preference.Item(key.melodyUri, def.melodyUri, preferenceRepo.melodyUri),
            Preference.Item(key.volume, def.volume, preferenceRepo.volume),
            Preference.Item(key.volumeIncrease, def.volumeIncrease, preferenceRepo.volumeIncrease),
            Preference.Title(R.string.pref_header_other),
            Preference.Item(key.isDeveloper, def.isDeveloper, preferenceRepo.isDeveloper),
            Preference.Title(R.string.pref_header_path),
            Preference.Custom(R.string.pref_title_path_app, fileControl.appDirectory.path),
            Preference.Custom(R.string.pref_title_path_cache, fileControl.cacheDirectory.path),
            Preference.Custom(R.string.pref_title_path_save, fileControl.saveDirectory.path),
            Preference.Title(R.string.pref_header_backup_files)
        )

        // TODO replace cache and app paths
        //        ContextCompat.getExternalFilesDirs()
        //        ContextCompat.getExternalCacheDirs()

        list.addAll(fileControl.getFileList(FileType.BACKUP).map { Preference.File(it) })

        return list
    }

}