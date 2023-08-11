package sgtmelon.scriptum.data.dataSource.system

import sgtmelon.scriptum.infrastructure.model.item.FileItem
import sgtmelon.scriptum.infrastructure.model.key.FileType
import java.io.File

interface FileDataSource {

    fun readFileFromPath(value: String): String?

    fun readFileFromUri(value: String): String?

    fun writeFile(name: String, data: String): String?


    fun getTimeName(time: FileType): String

    val savePath: String

    val externalDirectory: File

    val externalFiles: List<File>

    val externalCache: List<File>

    suspend fun getBackupFileList(): List<FileItem>
}