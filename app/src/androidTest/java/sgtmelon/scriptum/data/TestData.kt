package sgtmelon.scriptum.data

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getCheck
import sgtmelon.scriptum.extension.getTime
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.room.IRoomWork
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.room.entity.RollEntity
import java.util.UUID.randomUUID
import kotlin.random.Random

class TestData(override val context: Context, private val iPreferenceRepo: IPreferenceRepo) :
        IRoomWork {

    val uniqueString get() = randomUUID().toString().substring(0, 16)

    val textNote: NoteEntity
        get() = NoteEntity().apply {
            create = context.getTime()
            change = context.getTime()
            name = context.getString(R.string.test_note_name)
            text = context.getString(R.string.test_note_text)
            color = (0 until ColorData.size).random()
            type = NoteType.TEXT
        }

    val rollNote: NoteEntity
        get() = NoteEntity().apply {
            create = context.getTime()
            change = context.getTime()
            name = context.getString(R.string.test_note_name)
            setCompleteText(rollList.getCheck(), rollList.size)
            color = (0 until ColorData.size).random()
            type = NoteType.ROLL
        }

    val rollList = object : ArrayList<RollEntity>() {
        init {
            for (i in 0 until 10) {
                add(RollEntity().apply {
                    position = i
                    isCheck = Random.nextBoolean()
                    text = i.toString() + " | " + context.getString(R.string.test_roll_text)
                })
            }
        }
    }

    val rankEntity: RankEntity get() = RankEntity(name = uniqueString)


    fun createText() = NoteModel.getCreate(
            context.getTime(), iPreferenceRepo.defaultColor, NoteType.TEXT
    )

    fun createRoll() = NoteModel.getCreate(
            context.getTime(), iPreferenceRepo.defaultColor, NoteType.ROLL
    )


    fun insertRank(rank: RankEntity = rankEntity): RankEntity {
        inRoom { rank.id = iRankDao.insert(rank) }

        return rank
    }

    fun insertRankForNotes(): RankEntity {
        val noteModel = if (Random.nextBoolean()) insertText() else insertRoll()

        val rankEntity = insertRank(rankEntity.apply {
            noteId.add(noteModel.noteEntity.id)
        })

        noteModel.noteEntity.rankId = rankEntity.id
        noteModel.noteEntity.rankPs = rankEntity.position

        inRoom { iNoteDao.update(noteModel.noteEntity) }

        return rankEntity
    }

    fun insertRankForBin(): RankEntity  {
        val noteModel = if (Random.nextBoolean()) insertTextToBin() else insertRollToBin()

        val rankEntity = insertRank(rankEntity.apply {
            noteId.add(noteModel.noteEntity.id)
        })

        noteModel.noteEntity.rankId = rankEntity.id
        noteModel.noteEntity.rankPs = rankEntity.position

        inRoom { iNoteDao.update(noteModel.noteEntity) }

        return rankEntity
    }

    fun insertText(note: NoteEntity = textNote): NoteModel {
        inRoom { note.id = iNoteDao.insert(note) }

        return NoteModel(note)
    }

    fun insertTextToBin(note: NoteEntity = textNote) = insertText(note.apply { isBin = true })

    fun insertRoll(note: NoteEntity = rollNote, list: ArrayList<RollEntity> = rollList): NoteModel {
        inRoom {
            note.id = iNoteDao.insert(note)
            list.forEach {
                it.noteId = note.id
                iRollDao.insert(it)
            }
        }

        return NoteModel(note, list)
    }

    fun insertRollToBin(note: NoteEntity = rollNote, list: ArrayList<RollEntity> = rollList) =
            insertRoll(note.apply { isBin = true }, list)

    fun insertNotification(noteModel: NoteModel): NoteModel {
        inRoom {
            iAlarmDao.insert(AlarmEntity(noteId = noteModel.noteEntity.id, date = context.getTime()))
        }

        return noteModel
    }


    fun fillRank(count: Int = 10) = ArrayList<RankEntity>().apply {
        (0 until count).forEach {
            add(insertRank(rankEntity.apply {
                name = "$it | $name"
                position = it
                isVisible = Random.nextBoolean()
            }))
        }
    }

    fun fillNotes(count: Int = 10) = repeat(count) {
        if (Random.nextBoolean()) insertText() else insertRoll()
    }

    fun fillBin(count: Int = 10) = repeat(count) {
        if (Random.nextBoolean()) insertTextToBin() else insertRollToBin()
    }

    fun fillNotification(count: Int = 10) = repeat(count) {
        insertNotification(if (Random.nextBoolean()) insertText() else insertRoll())
    }


    fun clear() = apply { inRoom { clearAllTables() } }

}