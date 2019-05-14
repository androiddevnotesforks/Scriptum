package sgtmelon.scriptum.repository

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.RankModel
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.NoteType

/**
 * Интерфейс для общения с [RoomRepo]
 *
 * @author SerjantArbuz
 */
interface IRoomRepo {

    fun getNoteModelList(bin: Boolean): MutableList<NoteModel>

    suspend fun clearBin()

    suspend fun deleteNote(item: NoteItem)

    suspend fun restoreNote(item: NoteItem)

    /**
     * Полное удаление заметки из базы данных и очистка категорий от неё
     */
    suspend fun clearNote(item: NoteItem)

    fun getRankIdVisibleList(): List<Long>

    fun getRankCount(): Boolean

    /**
     * @throws NullPointerException когда заметка не сохранена в базу данных и [NoteItem.id] == [NoteData.Default.ID]
     */
    fun getNoteModel(id: Long): NoteModel

    /**
     * @return Список с именами всех категорий
     */
    fun getRankNameList(): List<String>

    /**
     * @return Булевый массив с true - для категорий, принадлежащих заметке
     */
    fun getRankCheckArray(noteItem: NoteItem): BooleanArray

    /**
     * @throws ClassCastException если [NoteItem.type] != [NoteType.TEXT]
     */
    fun convertToRoll(noteModel: NoteModel): NoteModel

    /**
     * @throws ClassCastException если [NoteItem.type] != [NoteType.ROLL]
     */
    fun convertToText(noteModel: NoteModel): NoteModel

    /**
     * @throws ClassCastException если [NoteItem.type] != [NoteType.ROLL]
     */
    fun getRollListString(noteItem: NoteItem): String

    /**
     * @throws ClassCastException если [NoteItem.type] != [NoteType.ROLL]
     */
    fun getRollStatusString(noteItem: NoteItem): String

    fun getRankIdList(): List<Long>

    /**
     * @throws ClassCastException если [NoteItem.type] != [NoteType.TEXT]
     */
    fun saveTextNote(noteModel: NoteModel, isCreate: Boolean): NoteModel

    /**
     * @throws ClassCastException если [NoteItem.type] != [NoteType.ROLL]
     */
    fun saveRollNote(noteModel: NoteModel, isCreate: Boolean): NoteModel

    fun insertRank(p: Int, rankItem: RankItem): Long

    /**
     * Обновление конкретного пункта списка
     */
    fun updateRollCheck(noteItem: NoteItem, rollItem: RollItem)

    /**
     * Обновление всех пунктов списка
     *
     * @param check состояние для всех пунктов
     */
    fun updateRollCheck(noteItem: NoteItem, check: Boolean)

    fun updateNote(noteItem: NoteItem)

    /**
     * Обновление всех прикреплённых заметок в статус баре
     */
    suspend fun notifyStatusBar()

    fun deleteRank(name: String, p: Int)

    fun getRankModel(): RankModel

    fun updateRank(dragFrom: Int, dragTo: Int): MutableList<RankItem>

    fun updateRank(rankItem: RankItem)

    fun updateRank(rankList: List<RankItem>)

}