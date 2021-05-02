package sgtmelon.scriptum.domain.interactor.impl

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.BackupRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.IBackupRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.data.room.backup.IBackupParser
import sgtmelon.scriptum.domain.interactor.callback.IBackupPrefInteractor
import sgtmelon.scriptum.domain.model.annotation.FileType
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.FileItem
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult
import sgtmelon.scriptum.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.presentation.control.file.IFileControl

/**
 * Interactor for import/export backup files.
 */
class BackupPrefInteractor(
    private val preferenceRepo: IPreferenceRepo,
    private val alarmRepo: IAlarmRepo,
    private val rankRepo: IRankRepo,
    private val noteRepo: INoteRepo,
    private val backupRepo: IBackupRepo,
    private val backupParser: IBackupParser,
    private val fileControl: IFileControl,
    private val cipherControl: ICipherControl
) : IBackupPrefInteractor {

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