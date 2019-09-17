package sgtmelon.scriptum

/**
 * Описание сценариев для UI тестов
 *
 * Значения:
 * UI - Проверяется UI тестом
 * CONTROL - Проверяется UI тестом с контролем тестировщика
 * MANUAL - Проверяется вручную тестировщиком
 *
 * Для поворота экрана использовать ctrl + left/rightArrow
 */
@Suppress("unused")
private class Scenario {

    class TODO {

        /**
         * TODO:
         * Тест сортировки заметок
         * Тест перетаскивания категорий
         * Тесты с нагрузкой
         * Тесты для автоматического сохранения (отдельный класс)
         */

        /**
         * TODO Сценарий - При нажатии на кнопку отмены тулбара, после автоматического сохранения, заметка закрывалась, а не переходила в режим просмотра
         * ... Не обновлялось id внутри Text/RollNoteViewModel
         * # Для текста
         * # Для списка
         */

        /**
         * TODO Сценарий - После конвертирования заметки и попытке открепить её от статус бара (через кнопку в уведомлении) информация в заметке не обновлялась
         * ... не обновлялся id внутри NoteViewModel
         * # Для текста
         * # Для списка
         */

        /**
         * TODO Сценарий - После конвертирования заметки и смены ориентации отображался не тот фрагмент
         * ... не обновлялся noteType внутри NoteViewModel
         * # Для текста
         * # Для списка
         */

        /**
         * TODO Сценарий - Проверка после конвертирования со сменой данных (открыть заметку - конвертировать - сменить данные - конвертировать)
         * # Начать с TextNote
         * # Начать с RollNote
         */

        // TODO после результата переименовывания диалога обновлять тулбар

    }

    /**
     * Сценарии для тестов выполняющихся автоматически
     */
    class Auto {

        /**
         * TODO
         */
        class Preference

    }

    class Integration {

        // TODO проверка не удаления скрытых заметок из базы данных
        // TODO сценарий проверки правильности получения данных при отсутствии их в бд (удалил alarmItem, а noteModel пытается получить её и NPE commit 18.06.19 (62ecee45))

    }

}