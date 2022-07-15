package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference

import sgtmelon.scriptum.infrastructure.preferences.IPreferenceRepo
import sgtmelon.scriptum.cleanup.data.repository.room.BackupRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IBackupRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IBackupPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.FileType
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.domain.model.item.FileItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.domain.model.result.ExportResult
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult
import sgtmelon.scriptum.cleanup.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.cleanup.presentation.control.file.IFileControl
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IBackupPreferenceViewModel

/**
 * Interactor for import/export backup files (for [IBackupPreferenceViewModel]).
 */
class BackupPreferenceInteractor(
    private val preferenceRepo: IPreferenceRepo,
    private val alarmRepo: IAlarmRepo,
    private val rankRepo: IRankRepo,
    private val noteRepo: INoteRepo,
    private val backupRepo: IBackupRepo,
    private val backupParser: IBackupParser,
    private val fileControl: IFileControl,
    private val cipherControl: ICipherControl
) : IBackupPreferenceInteractor {

    @RunPrivate var fileList: List<FileItem>? = null

    override suspend fun getFileList(): List<FileItem> {
        return fileList ?: fileControl.getFileList(FileType.BACKUP).also { fileList = it }
    }

    override fun resetFileList() {
        fileList = null
    }


    override suspend fun export(): ExportResult {
        val noteList = noteRepo.getNoteBackup()

        val noteIdList = noteList.filter { it.type == NoteType.ROLL }.map { it.id }

        val rollList = noteRepo.getRollBackup(noteIdList)
        val rollVisibleList = noteRepo.getRollVisibleBackup(noteIdList)
        val rankList = rankRepo.getRankBackup()
        val alarmList = alarmRepo.getAlarmBackup(noteIdList)

        val parserResult = ParserResult(noteList, rollList, rollVisibleList, rankList, alarmList)

        val data = backupParser.collect(parserResult)
        val encryptData = cipherControl.encrypt(data)

        val timeName = fileControl.getTimeName(FileType.BACKUP)
        val path = fileControl.writeFile(timeName, encryptData) ?: return ExportResult.Error

        return ExportResult.Success(path)
    }

    override suspend fun import(name: String): ImportResult {
        val list = getFileList()
        val item = list.firstOrNull { it.name == name } ?: return ImportResult.Error

        val encryptData = fileControl.readFile(item.path) ?: return ImportResult.Error
        val data = cipherControl.decrypt(encryptData)

        val parserResult = backupParser.parse(data) ?: return ImportResult.Error
        val importSkip = preferenceRepo.importSkip

        return backupRepo.insertData(BackupRepo.Model[parserResult], importSkip)
    }
}