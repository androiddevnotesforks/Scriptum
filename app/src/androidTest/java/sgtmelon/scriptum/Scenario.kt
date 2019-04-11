package sgtmelon.scriptum

import sgtmelon.scriptum.test.IntroTest
import sgtmelon.scriptum.test.SplashTest
import sgtmelon.scriptum.test.main.BinTest
import sgtmelon.scriptum.test.main.MainTest
import sgtmelon.scriptum.test.main.NotesTest
import sgtmelon.scriptum.test.main.RankTest

/**
 * Описание сценариев для тестов
 *
 * Значения:
 * UI - Проверяется UI тестом
 * UI/CONTROL - Проверяется UI тестом с контролем тестировщика
 * MANUAL - Проверяется вручную тестировщиком
 *
 * @author SerjantArbuz
 */
@Suppress("unused")
private class Scenario {

    /**
     * Сценарии для [SplashTest]
     */
    class Splash {

        /**
         * UI - Открытие приветствия :: [SplashTest.introScreenOpen]
         */

        /**
         * UI - Открытие главного экрана :: [SplashTest.mainScreenOpen]
         */

        /**
         * UI - Открытие заметок через уведомление
         * # Текст :: [SplashTest.statusTextNoteOpen]
         * # Список :: [SplashTest.statusRollNoteOpen]
         */

    }

    /**
     * Сценарии для [IntroTest]
     */
    class Intro {

        /**
         * UI - Расположение контента на страницах :: [IntroTest.contentPlacement]
         */

        /**
         * UI - Доступ к кнопке end :: [IntroTest.endButtonEnable]
         */

        /**
         * UI - Работа кнопки end :: [IntroTest.endButtonWork]
         */

    }

    /**
     * Сценарии для [MainTest]
     */
    class Main {

        /**
         * UI - Стартовый экран :: [MainTest.startScreen]
         */

        /**
         * UI - Отображение правильного экрана при нажатии на пункт меню :: [MainTest.menuClickCorrectScreen]
         */

        /**
         * UI - Отображения кнопки добавить :: [MainTest.addFabVisible]
         */

        /**
         * UI - Работа диалога добавления заметки
         * # Открытие диалога :: [MainTest.addDialogOpen]
         * # Создать текст :: [MainTest.addDialogCreateTextNote]
         * # Создать список :: [MainTest.addDialogCreateRollNote]
         */

        /**
         * UI/CONTROL - Скроллинга страниц до верха
         * # Категории :: [MainTest.rankScreenScrollTop]
         * # Заметки :: [MainTest.notesScreenScrollTop]
         * # Корзина :: [MainTest.binScreenScrollTop]
         */

        /**
         * MANUAL - Поворот экрана
         * # Диалог добавления заметки
         */

    }

    /**
     * Сценарии для [RankTest]
     */
    class Rank { // TODO

        /**
         * UI - Контент
         * # Пусто :: [RankTest.contentEmpty]
         * # Список :: [RankTest.contentList]
         **/

        /**
         * UI/CONTROL - Скроллинг списка :: [RankTest.listScroll]
         */

        /**
         * UI - Работа toolbar'а добавления категории
         * # Доступ к кнопке добавить :: [RankTest.toolbarEnterAddEnable]
         * # Работа кнопки отчистки :: [RankTest.toolbarEnterClear]
         * # Добавление категории в начало :: [RankTest.toolbarEnterAddStart]
         * # Добавление категории в конец :: [RankTest.toolbarEnterAddEnd]
         */

        /**
         * UI - Управление категорией
         * # Видимость категории :: [RankTest.rankVisible]
         * # Удаление категории :: [RankTest.rankClear]
         */

        /**
         * UI - Работа диалога переименовывания :: [RankTest.renameDialogWork]
         */

        /**
         * MANUAL - Поворот экрана
         * # Контент
         * # Диалог переименовывания
         */

    }

    /**
     * Сценарии для [NotesTest]
     */
    class Notes {

        /**
         * UI - Контент
         * # Пусто :: [NotesTest.contentEmpty]
         * # Список :: [NotesTest.contentList]
         **/

        /**
         * UI - Открытие настроек :: [NotesTest.openPreference]
         */

        /**
         * UI - Видимость кнопки добавить при скроллинге :: [NotesTest.addFabVisibleOnScroll]
         */

        /**
         * UI - Открытие заметки
         * # Текст :: [NotesTest.textNoteOpen]
         * #  Список :: [NotesTest.rollNoteOpen]
         */

        /**
         * UI - Создание заметки и возврат назад без сохранения
         * # Текст :: [NotesTest.textNoteCreateAndReturn]
         * # Список :: [NotesTest.rollNoteCreateAndReturn]
         */

        /**
         * UI - Создание заметки и возврат назад с сохранением
         * #  Текст :: [NotesTest.textNoteCreateAndReturnWithSave]
         * #  Список :: [NotesTest.rollNoteCreateAndReturnWithSave]
         */

        /**
         * UI - Работа диалога управления заметкой
         * #  Текст:
         * #  Открыть :: [NotesTest.textNoteDialogOpen]
         * #  Прикрепить к статус бару :: [NotesTest.textNoteDialogBind]
         * #  Открепить от статус бара :: [NotesTest.textNoteDialogUnbind]
         * #  Конвертировать :: [NotesTest.textNoteDialogConvert]
         * #  Копировать текст :: [NotesTest.textNoteDialogCopy]
         * #  Удаление :: [NotesTest.textNoteDialogDelete]
         *
         * #  Список:
         * #  Открыть :: [NotesTest.rollNoteDialogOpen]
         * # Выполнить всё :: [NotesTest.rollNoteDialogCheckAll]
         * # Снять выделения :: [NotesTest.rollNoteDialogUncheckAll]
         * # Прикрепить к статус бару :: [NotesTest.rollNoteDialogBind]
         * #  Открепить от статус бара :: [NotesTest.rollNoteDialogUnbind]
         * #  Конвертировать :: [NotesTest.rollNoteDialogConvert]
         * #  Копировать текст :: [NotesTest.rollNoteDialogCopy]
         * #  Удаление :: [NotesTest.rollNoteDialogDelete]
         */

        /**
         * MANUAL - Поворот экрана
         * # Контент
         * # Диалог управления заметкой
         */

    }

    /**
     * Сценарии для [BinTest]
     */
    class Bin {

        /**
         * UI - Контент
         * #  Пусто :: [BinTest.contentEmpty]
         * #  Список :: [BinTest.contentList]
         **/

        /**
         * UI/CONTROL - Скроллинг списка :: [BinTest.listScroll]
         */

        /**
         * UI - Открытие заметки
         * #  Текст :: [BinTest.textNoteOpen]
         * #  Список :: [BinTest.rollNoteOpen]
         */

        /**
         * UI - Работы диалога отчистки корзины :: [BinTest.clearDialogWork]
         */

        /**
         * UI - Работа диалога управления заметкой
         * #  Текст:
         * #  Открыть :: [BinTest.textNoteDialogOpen]
         * #  Восстановить :: [BinTest.textNoteDialogRestore]
         * #  Копировать текст :: [BinTest.textNoteDialogCopy]
         * #  Отчистить :: [BinTest.textNoteDialogClear]
         *
         * #  Список:
         * #  Открыть :: [BinTest.rollNoteDialogOpen]
         * #  Восстановить :: [BinTest.rollNoteDialogRestore]
         * #  Копировать текст :: [BinTest.rollNoteDialogCopy]
         * #  Отчистить :: [BinTest.rollNoteDialogClear]
         */

        /**
         * MANUAL - Поворот экрана
         * #  Контент
         * # Диалог отчистки корзины
         * # Диалог управления заметкой
         */

    }

}