package sgtmelon.scriptum.data.dataSource.system

import java.io.File
import sgtmelon.scriptum.infrastructure.model.item.FileItem
import sgtmelon.scriptum.infrastructure.model.type.FileType

interface FileDataSource {

    val saveDirectory: File

    fun getExternalFiles(): List<File>

    fun getExternalCache(): List<File>


    fun readFile(path: String): String?

    fun writeFile(name: String, data: String): String?

    fun getTimeName(@FileType type: String): String


    suspend fun getBackupFileList(): List<FileItem>

}