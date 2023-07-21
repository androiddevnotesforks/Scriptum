package sgtmelon.scriptum.data.dataSource.system

import sgtmelon.scriptum.infrastructure.model.item.FileItem
import sgtmelon.scriptum.infrastructure.model.key.FileType
import java.io.File

interface FileDataSource {

    val saveDirectory: File

    fun getExternalFiles(): List<File>

    fun getExternalCache(): List<File>


    fun readFile(path: String): String?

    fun writeFile(name: String, data: String): String?

    fun getTimeName(time: FileType): String

    suspend fun getBackupFileList(): List<FileItem>
}