package sgtmelon.scriptum.repository

interface IDevelopRepo {

    suspend fun getNoteTableData() : String

    suspend fun getRollTableData() : String

    suspend fun getRankTableData() : String

}