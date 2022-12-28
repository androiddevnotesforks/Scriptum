package sgtmelon.scriptum.infrastructure.backup

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.dataSource.backup.BackupDataSource

class BackupDataSourceImpl(private val context: Context) : BackupDataSource {

    override val versionKey: String
        get() = context.getString(R.string.backup_version)

    override val hashKey: String
        get() = context.getString(R.string.backup_hash)

    override val databaseKey: String
        get() = context.getString(R.string.backup_database)
}