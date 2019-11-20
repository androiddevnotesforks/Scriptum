package sgtmelon.scriptum

import sgtmelon.scriptum.test.auto.note.RollNoteTest
import sgtmelon.scriptum.test.auto.note.TextNoteTest
import sgtmelon.scriptum.test.content.note.ParentNoteContentTest
import sgtmelon.scriptum.test.control.NoteIconAnimTest
import sgtmelon.scriptum.test.control.rotation.note.RollNoteRotationTest
import sgtmelon.scriptum.test.control.rotation.note.TextNoteRotationTest

// EXAMPLE: TODO(reason = "#TEST write test")

class Content {
    /**
     * TODO #TEST finish tests for:
     * 1. [ParentNoteContentTest.rankSort]
     * 2. [ParentNoteContentTest.rankTextCancel]
     * 3. [ParentNoteContentTest.rankRollCancel]
     */

    // TODO #TEST create AlarmContentTest (test notesAdapter)
    // TODO #TEST create RollNoteContentTest (test rollAdapter)
}

class Rank {
    // TODO #TEST rank drag test
}

class Note {
    /**
     * TODO #TEST - При нажатии на кнопку отмены тулбара, после автоматического сохранения, заметка закрывалась, а не переходила в режим просмотра
     * ... Не обновлялось id внутри Text/RollNoteViewModel
     * # Для текста
     * # Для списка
     */

    /**
     * TODO #TEST - После конвертирования заметки и попытке открепить её от статус бара (через кнопку в уведомлении) информация в заметке не обновлялась
     * ... не обновлялся id внутри NoteViewModel
     * # Для текста
     * # Для списка
     */

    /**
     * TODO #TEST - После конвертирования заметки и смены ориентации отображался не тот фрагмент
     * ... не обновлялся noteType внутри NoteViewModel
     * # Для текста
     * # Для списка
     */

    /**
     * TODO #TEST - Проверка после конвертирования со сменой данных (открыть заметку - конвертировать - сменить данные - конвертировать)
     * # Начать с TextNote
     * # Начать с RollNote
     */

    /**
     * TODO #TEST
     * 1. [TextNoteTest.convertDialogCloseAndWork]
     * 2. [RollNoteTest.convertDialogCloseAndWork]
     * 3. [TextNoteTest.rankDialogCloseAndWork]
     * 4. [RollNoteTest.rankDialogCloseAndWork]
     */
}

class Rotation {
    /**
     * TODO #TEST Finish tests:
     * 1. [TextNoteRotationTest.convertDialogResult]
     * 2. [RollNoteRotationTest.convertDialogResult]
     */
}

class AutoSave {
    /**
     * TODO #TEST animation from arrow to cross on text/roll auto save
     * [NoteIconAnimTest]
     */
}

class Match {
    // TODO #TEST match backgroundColor
}

class Integration {

    // TODO #TEST проверка не удаления скрытых заметок из базы данных
    // TODO #TEST сценарий проверки правильности получения данных при отсутствии их в бд (удалил alarmItem, а noteModel пытается получить её и NPE commit 18.06.19 (62ecee45))

}