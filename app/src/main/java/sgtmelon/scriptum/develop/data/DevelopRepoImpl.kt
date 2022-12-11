package sgtmelon.scriptum.develop.data

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.data.dataSource.database.RollVisibleDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem.Preference as PrintPref

/**
 * Repository which work with all application data and needed only for developers.
 */
class DevelopRepoImpl(
    private val noteDataSource: NoteDataSource,
    private val rollDataSource: RollDataSource,
    private val rollVisibleDataSource: RollVisibleDataSource,
    private val rankDataSource: RankDataSource,
    private val alarmDataSource: AlarmDataSource,
    private val key: PreferencesKeyProvider,
    private val def: PreferencesDefProvider,
    private val preferences: Preferences,
    private val fileDataSource: FileDataSource
) : DevelopRepo {

    override suspend fun getPrintNoteList(isBin: Boolean): List<PrintItem.Note> {
        return noteDataSource.getList(isBin).map { PrintItem.Note(it) }
    }

    override suspend fun getPrintRollList(): List<PrintItem.Roll> {
        return rollDataSource.getList().map { PrintItem.Roll(it) }
    }

    override suspend fun getPrintVisibleList(): List<PrintItem.Visible> {
        return rollVisibleDataSource.getList().map { PrintItem.Visible(it) }
    }

    override suspend fun getPrintRankList(): List<PrintItem.Rank> {
        return rankDataSource.getList().map { PrintItem.Rank(it) }
    }

    override suspend fun getPrintAlarmList(): List<PrintItem.Alarm> {
        return alarmDataSource.getList().map { PrintItem.Alarm(it) }
    }

    override suspend fun getPrintPreferenceList(): List<PrintPref> = with(preferences) {
        return listOf(
            PrintPref.Title(R.string.pref_header_app),
            PrintPref.Key(key.isFirstStart, def.isFirstStart, isFirstStart),
            PrintPref.Key(key.theme, def.theme, theme),
            PrintPref.Title(R.string.pref_title_backup),
            PrintPref.Key(key.isBackupSkipImports, def.isBackupSkipImports, isBackupSkipImports),
            PrintPref.Title(R.string.pref_title_note),
            PrintPref.Key(key.sort, def.sort, sort),
            PrintPref.Key(key.defaultColor, def.defaultColor, defaultColor),
            PrintPref.Key(key.isPauseSaveOn, def.isPauseSaveOn, isPauseSaveOn),
            PrintPref.Key(key.isAutoSaveOn, def.isAutoSaveOn, isAutoSaveOn),
            PrintPref.Key(key.savePeriod, def.savePeriod, savePeriod),
            PrintPref.Title(R.string.pref_title_alarm),
            PrintPref.Key(key.repeat, def.repeat, repeat),
            PrintPref.Key(key.signal, def.signal, signal),
            PrintPref.Key(key.melodyUri, def.melodyUri, melodyUri),
            PrintPref.Key(key.volumePercent, def.volumePercent, volumePercent),
            PrintPref.Key(key.isVolumeIncrease, def.isVolumeIncrease, isVolumeIncrease),
            PrintPref.Title(R.string.pref_header_other),
            PrintPref.Key(key.isDeveloper, def.isDeveloper, isDeveloper)
        )
    }

    override suspend fun getPrintFileList(): List<PrintPref> {
        val list = mutableListOf(
            PrintPref.Title(R.string.pref_header_path_save),
            PrintPref.Path(fileDataSource.saveDirectory)
        )

        list.add(PrintPref.Title(R.string.pref_header_path_files))
        for (it in fileDataSource.getExternalFiles()) {
            list.add(PrintPref.Path(it))
        }

        list.add(PrintPref.Title(R.string.pref_header_path_cache))
        for (it in fileDataSource.getExternalCache()) {
            list.add(PrintPref.Path(it))
        }

        list.add(PrintPref.Title(R.string.pref_header_backup_files))
        for (it in fileDataSource.getBackupFileList()) {
            list.add(PrintPref.File(it))
        }

        return list
    }

    override suspend fun getRandomNoteId(): Long {
        return noteDataSource.getList(isBin = false).random().id
    }

    override fun resetPreferences() = preferences.clear()
}