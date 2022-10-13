package sgtmelon.scriptum.data.repository.database

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource
import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.data.dataSource.database.RankDataSource
import sgtmelon.scriptum.data.dataSource.database.RollDataSource
import sgtmelon.scriptum.data.dataSource.database.RollVisibleDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.infrastructure.model.item.PrintItem
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider

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

    override suspend fun getPrintPreferenceList(): List<PrintItem.Preference> {
        return listOf(
            PrintItem.Preference.Title(R.string.pref_header_app),
            PrintItem.Preference.Key(key.isFirstStart, def.isFirstStart, preferences.isFirstStart),
            PrintItem.Preference.Key(key.theme, def.theme, preferences.theme),
            PrintItem.Preference.Title(R.string.pref_title_backup),
            PrintItem.Preference.Key(
                key.isBackupSkipImports, def.isBackupSkipImports, preferences.isBackupSkipImports
            ),
            PrintItem.Preference.Title(R.string.pref_title_note),
            PrintItem.Preference.Key(key.sort, def.sort, preferences.sort),
            PrintItem.Preference.Key(key.defaultColor, def.defaultColor, preferences.defaultColor),
            PrintItem.Preference.Key(
                key.isPauseSaveOn, def.isPauseSaveOn, preferences.isPauseSaveOn
            ),
            PrintItem.Preference.Key(key.isAutoSaveOn, def.isAutoSaveOn, preferences.isAutoSaveOn),
            PrintItem.Preference.Key(key.savePeriod, def.savePeriod, preferences.savePeriod),
            PrintItem.Preference.Title(R.string.pref_title_alarm),
            PrintItem.Preference.Key(key.repeat, def.repeat, preferences.repeat),
            PrintItem.Preference.Key(key.signal, def.signal, preferences.signal),
            PrintItem.Preference.Key(key.melodyUri, def.melodyUri, preferences.melodyUri),
            PrintItem.Preference.Key(
                key.volumePercent,
                def.volumePercent,
                preferences.volumePercent
            ),
            PrintItem.Preference.Key(
                key.isVolumeIncrease, def.isVolumeIncrease, preferences.isVolumeIncrease
            ),
            PrintItem.Preference.Title(R.string.pref_header_other),
            PrintItem.Preference.Key(key.isDeveloper, def.isDeveloper, preferences.isDeveloper)
        )
    }

    override suspend fun getPrintFileList(): List<PrintItem.Preference> {
        val list = mutableListOf(
            PrintItem.Preference.Title(R.string.pref_header_path_save),
            PrintItem.Preference.Path(fileDataSource.saveDirectory)
        )

        list.add(PrintItem.Preference.Title(R.string.pref_header_path_files))
        for (it in fileDataSource.getExternalFiles()) {
            list.add(PrintItem.Preference.Path(it))
        }

        list.add(PrintItem.Preference.Title(R.string.pref_header_path_cache))
        for (it in fileDataSource.getExternalCache()) {
            list.add(PrintItem.Preference.Path(it))
        }

        list.add(PrintItem.Preference.Title(R.string.pref_header_backup_files))
        for (it in fileDataSource.getBackupFileList()) {
            list.add(PrintItem.Preference.File(it))
        }

        return list
    }

    override suspend fun getRandomNoteId(): Long {
        return noteDataSource.getList(isBin = false).random().id
    }

    override fun resetPreferences() = preferences.clear()
}