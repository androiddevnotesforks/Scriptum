package sgtmelon.scriptum.repository.room

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Interface for communicate with [RoomRepo]
 */
interface IRoomRepo {

    fun getNoteModelList(bin: Boolean): MutableList<NoteModel>

    /**
     * Have hide notes in list or not
     */
    fun isListHide(bin: Boolean): Boolean

    suspend fun clearBin()

    suspend fun deleteNote(noteModel: NoteModel)

    suspend fun restoreNote(noteModel: NoteModel)

    /**
     * Полное удаление заметки из базы данных и очистка категорий от неё
     */
    suspend fun clearNote(noteModel: NoteModel)

    fun getRankIdVisibleList(): List<Long>

    fun getRankCount(): Boolean

    /**
     * Return null if note not found in DataBase or [NoteEntity.id] == [NoteData.Default.ID]
     */
    fun getNoteModel(id: Long): NoteModel?

    /**
     * @return Список с именами всех категорий
     */
    fun getRankDialogItemArray(): Array<String>

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.TEXT]
     */
    fun convertToRoll(noteModel: NoteModel): NoteModel

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.ROLL]
     */
    fun convertToText(noteModel: NoteModel): NoteModel

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.ROLL]
     */
    fun getRollListString(noteEntity: NoteEntity): String

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.ROLL]
     */
    fun getRollStatusString(noteEntity: NoteEntity): String

    fun getRankIdList(): List<Long>

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.TEXT]
     */
    fun saveTextNote(noteModel: NoteModel, isCreate: Boolean): NoteModel

    /**
     * @throws ClassCastException если [NoteEntity.type] != [NoteType.ROLL]
     */
    fun saveRollNote(noteModel: NoteModel, isCreate: Boolean): NoteModel


    /**
     * Обновление конкретного пункта списка
     */
    fun updateRollCheck(noteEntity: NoteEntity, rollEntity: RollEntity)

    /**
     * Обновление всех пунктов списка
     *
     * @param check состояние для всех пунктов
     */
    fun updateRollCheck(noteEntity: NoteEntity, check: Boolean)

    fun updateNote(noteEntity: NoteEntity)

}