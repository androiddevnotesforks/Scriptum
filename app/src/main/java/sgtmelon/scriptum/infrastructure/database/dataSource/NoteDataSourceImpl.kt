package sgtmelon.scriptum.infrastructure.database.dataSource

import sgtmelon.scriptum.data.dataSource.database.NoteDataSource
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao

class NoteDataSourceImpl(private val dao: NoteDao) : NoteDataSource {

}