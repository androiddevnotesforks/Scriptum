package sgtmelon.scriptum.data.repository.database

import sgtmelon.scriptum.infrastructure.develop.PrintItem

interface DevelopRepo {

    suspend fun getPrintNoteList(isBin: Boolean): List<PrintItem.Note>

    suspend fun getPrintRollList(): List<PrintItem.Roll>

    suspend fun getPrintVisibleList(): List<PrintItem.Visible>

    suspend fun getPrintRankList(): List<PrintItem.Rank>

    suspend fun getPrintAlarmList(): List<PrintItem.Alarm>

    suspend fun getPrintPreferenceList(): List<PrintItem.Preference>

    suspend fun getPrintFileList(): List<PrintItem.Preference>

    suspend fun getRandomNoteId(): Long

    fun resetPreferences()
}