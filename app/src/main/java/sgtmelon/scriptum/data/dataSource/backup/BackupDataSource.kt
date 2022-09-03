package sgtmelon.scriptum.data.dataSource.backup

interface BackupDataSource {

    val versionKey: String

    val hashKey: String

    val databaseKey: String
}