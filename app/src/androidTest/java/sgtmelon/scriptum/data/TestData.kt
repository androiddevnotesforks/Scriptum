package sgtmelon.scriptum.data

import android.content.Context
import sgtmelon.extension.getTime
import sgtmelon.scriptum.basic.extension.getFutureTime
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.room.IRoomWork
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
            create = getTime()
            change = getTime()
            name = uniqueString
            text = uniqueString.repeat(n = (1 until 10).random())
            color = (0 until ColorData.size).random()
            type = NoteType.TEXT
        }

    val rollNote: NoteEntity
        get() = NoteEntity().apply {
            create = getTime()
            change = getTime()
            name = uniqueString
            color = (0 until ColorData.size).random()
            type = NoteType.ROLL
        }

    val rollList: ArrayList<RollEntity>
        get() = ArrayList<RollEntity>().apply {
            (0 until (1 until 6).random()).forEach {
                add(rollEntity.apply {
                    position = it
                    text = "$it | $text"
                })
            }
        }

    val rollEntity: RollEntity
        get() = RollEntity().apply {
            isCheck = Random.nextBoolean()
            text = uniqueString
        }

    val rankEntity: RankEntity get() = RankEntity(name = uniqueString)


    fun createText() = NoteModel.getCreate(getTime(), iPreferenceRepo.defaultColor, NoteType.TEXT)

    fun createRoll() = NoteModel.getCreate(getTime(), iPreferenceRepo.defaultColor, NoteType.ROLL)


    fun insertRank(rank: RankEntity = rankEntity): RankEntity {
        inRoom { rank.id = iRankDao.insert(rank) }

        return rank
    }

    fun insertRankForNotes(): RankEntity {
        val noteModel = insertNote()

        val rankEntity = insertRank(rankEntity.apply {
            noteId.add(noteModel.noteEntity.id)
        })

        inRoom {
            iNoteDao.update(noteModel.noteEntity.apply {
                rankId = rankEntity.id
                rankPs = rankEntity.position
            })
        }

        return rankEntity
    }

    fun insertRankForBin(): RankEntity {
        val noteModel = insertNoteToBin()

        val rankEntity = insertRank(rankEntity.apply {
            noteId.add(noteModel.noteEntity.id)
        })

        inRoom {
            iNoteDao.update(noteModel.noteEntity.apply {
                rankId = rankEntity.id
                rankPs = rankEntity.position
            })
        }

        return rankEntity
    }

    fun insertText(note: NoteEntity = textNote): NoteModel {
        if (note.type != NoteType.TEXT) throw IllegalAccessException("Wrong note type")

        inRoom { note.id = iNoteDao.insert(note) }

        return NoteModel(note)
    }

    fun insertTextToBin(note: NoteEntity = textNote) = insertText(note.apply { isBin = true })

    fun insertRoll(note: NoteEntity = rollNote, list: ArrayList<RollEntity> = rollList): NoteModel {
        if (note.type != NoteType.ROLL) throw IllegalAccessException("Wrong note type")

        note.setCompleteText(list)

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


    fun insertNote(): NoteModel = if (Random.nextBoolean()) insertText() else insertRoll()

    fun insertNoteToBin(): NoteModel =
            if (Random.nextBoolean()) insertTextToBin() else insertRollToBin()

    fun insertNotification(noteModel: NoteModel = insertNote(), date: String = getFutureTime()): NoteModel {
        noteModel.alarmEntity.date = date

        inRoom { noteModel.alarmEntity.id = iAlarmDao.insert(noteModel.alarmEntity) }

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

    fun fillRankRelation(count: Int = 10) = ArrayList<RankEntity>().apply {
        (0 until count).forEach {
            val noteCount = (0 until 5).random()
            val noteList = ArrayList<NoteModel>().apply {
                repeat(noteCount) { add(insertNote()) }
            }

            val rankEntity = insertRank(rankEntity.apply {
                name = "$it | $name"
                position = it
                isVisible = Random.nextBoolean()

                noteList.map { it.noteEntity.id }.forEach { noteId.add(it) }
            })

            add(rankEntity)

            inRoom {
                noteList.map { it.noteEntity }.forEach {
                    iNoteDao.update(it.apply {
                        rankId = rankEntity.id
                        rankPs = rankEntity.position
                    })
                }
            }
        }
    }

    fun fillNotes(count: Int = 10) = repeat(count) { insertNote() }

    fun fillBin(count: Int = 10) = repeat(count) { insertNoteToBin() }

    fun fillNotification(count: Int = 10) = repeat(count) {
        insertNotification(date = getFutureTime())
    }


    fun clear() = apply { inRoom { clearAllTables() } }

}