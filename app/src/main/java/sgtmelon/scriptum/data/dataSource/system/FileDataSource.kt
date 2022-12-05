package sgtmelon.scriptum.data.dataSource.system

import java.io.File
import sgtmelon.scriptum.infrastructure.model.item.FileItem

interface FileDataSource {

    val saveDirectory: File

    fun getExternalFiles(): List<File>

    fun getExternalCache(): List<File>


    fun readFile(path: String): String?

    fun writeFile(name: String, data: String): String?

    fun getBackupName(): String

    suspend fun getBackupFileList(): List<FileItem>
}