@file:Suppress("unused")

package sgtmelon.scriptum.cleanup

import sgtmelon.scriptum.cleanup.test.ui.control.anim.note.NoteIconAnimTest
import sgtmelon.scriptum.ui.cases.NoteCardTestCase

// EXAMPLE: TODO(reason = "#TEST write test")

class Content {
    /**
     * TODO #TEST finish tests for:
     * 1. [NoteCardTestCase.rankSort]
     * 2. [NoteCardTestCase.rankTextCancel]
     * 3. [NoteCardTestCase.rankRollCancel]
     */

    // TODO #TEST create AlarmContentTest (test notesAdapter)
    // TODO #TEST create RollNoteContentTest (test rollAdapter)
}

class Rank {
    // TODO #TEST rank drag test
}

class Note {
    /**
     * TODO #TEST - После конвертирования заметки и попытке открепить её от статус бара (через кнопку в уведомлении) информация в заметке не обновлялась
     * ... не обновлялся id внутри NoteViewModel
     * # Для текста
     * # Для списка
     */

    /**
     * TODO #TEST - Проверка после конвертирования со сменой данных (открыть заметку - конвертировать - сменить данные - конвертировать)
     * # Начать с TextNote
     * # Начать с RollNote
     */

    /**
     * TODO #TEST Error when addItems in rollNote and clear text in them -> updateComplete was before items remove
     * create roll - add items - clear text on some item - save and return to list -> check noteItem text
     */

    class RollEmptyInfo {
        // check show on note create
        // check empty info

        // info on restore
    }
}

class Rotation {
    /**
     * TODO #TEST - После конвертирования заметки и смены ориентации отображался не тот фрагмент
     * ... не обновлялся noteType внутри NoteViewModel
     * # Для текста
     * # Для списка
     */

    /**
     * TODO #TEST for preference screen
     */
}

class AutoSave {
    /**
     * TODO #TEST animation from arrow to cross on text/roll auto save
     * [NoteIconAnimTest]
     */

    /**
     * TODO #TEST - При нажатии на кнопку отмены тулбара, после автоматического сохранения, заметка закрывалась, а не переходила в режим просмотра
     * ... Не обновлялось id внутри Text/RollNoteViewModel
     * # Для текста
     * # Для списка
     */
}

class Integration {

    // TODO #TEST проверка не удаления скрытых заметок из базы данных
    // TODO #TEST сценарий проверки правильности получения данных при отсутствии их в бд (удалил alarmItem, а noteModel пытается получить её и NPE commit 18.06.19 (62ecee45))

}