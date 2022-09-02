package sgtmelon.scriptum.data.dataSource.system

import java.io.File
import sgtmelon.scriptum.cleanup.domain.model.annotation.FileType
import sgtmelon.scriptum.cleanup.domain.model.item.FileItem

/**
 * Interface for [FileDataSourceImpl].
 */
interface FileDataSource {

    val saveDirectory: File

    fun getExternalFiles(): List<File>

    fun getExternalCache(): List<File>


    fun readFile(path: String): String?

    fun writeFile(name: String, data: String): String?

    fun getTimeName(@FileType type: String): String


    suspend fun getFileList(@FileType type: String): List<FileItem>

}