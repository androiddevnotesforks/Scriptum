package sgtmelon.scriptum

import sgtmelon.scriptum.test.auto.IntroTest
import sgtmelon.scriptum.test.auto.SplashTest
import sgtmelon.scriptum.test.auto.main.BinTest
import sgtmelon.scriptum.test.auto.main.MainTest
import sgtmelon.scriptum.test.auto.main.NotesTest
import sgtmelon.scriptum.test.auto.main.RankTest
import sgtmelon.scriptum.test.auto.note.roll.*
import sgtmelon.scriptum.test.auto.note.text.*
import sgtmelon.scriptum.test.control.InfoAnimTest
import sgtmelon.scriptum.test.control.NoteToolbarIconTest
import sgtmelon.scriptum.test.control.RotationTest
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen
import sgtmelon.scriptum.ui.screen.main.RankScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Описание сценариев для UI тестов
 *
 * Значения:
 * UI - Проверяется UI тестом
 * CONTROL - Проверяется UI тестом с контролем тестировщика
 * MANUAL - Проверяется вручную тестировщиком
 *
 * Для поворота экрана использовать ctrl + left/rightArrow
 *
 * @author SerjantArbuz
 */
@Suppress("unused")
private class ScenarioUi {

    class TODO {

        /**
         * TODO:
         * Тест сортировки заметок
         * Тест перетаскивания категорий
         * Тесты с нагрузкой
         * Тесты для автоматического сохранения (отдельный класс)
         */

        // TODO - Вынести control тесты из auto

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

    }

    /**
     * Сценарии для тестов выполняющихся автоматически
     */
    class Auto {

        /**
         * Сценарии для [SplashTest]
         */
        class Splash {

            /**
             * UI - Открыть приветствие :: [SplashTest.introScreenOpen]
             */

            /**
             * UI - Открыть главный экран :: [SplashTest.mainScreenOpen]
             */

            /**
             * UI - Открыть заметку через уведомление
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
             * UI - Доступ к кнопке end :: [IntroTest.endButtonEnabled]
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
             * # Открыть :: [MainTest.addDialogOpen]
             *
             * # Закрыть:
             * # Кнопкой назад :: [MainTest.addDialogCloseSoft]
             * # Свайпом вниз :: [MainTest.addDialogCloseSwipe]
             *
             * # Создать текст :: [MainTest.addDialogCreateTextNote]
             * # Создать список :: [MainTest.addDialogCreateRollNote]
             */

            /**
             * CONTROL - Скроллинга страниц до верха
             * # Категории :: [MainTest.rankScreenScrollTop]
             * # Заметки :: [MainTest.notesScreenScrollTop]
             * # Корзина :: [MainTest.binScreenScrollTop]
             */

        }

        /**
         * Сценарии для [RankTest]
         */
        class Rank {

            /**
             * UI - Контент
             * # Пусто :: [RankTest.contentEmpty]
             * # Список :: [RankTest.contentList]
             **/

            /**
             * CONTROL - Скроллинг списка :: [RankTest.listScroll]
             */

            /**
             * UI - Работа toolbar'а добавления категории
             * # Доступ к кнопке добавить
             * # Пустой текст :: [RankTest.toolbarEnterAddEmpty]
             * # Текст из списка :: [RankTest.toolbarEnterAddFromList]
             * # Доступна :: [RankTest.toolbarEnterAddEnabled]
             *
             * # Работа кнопки отчистки :: [RankTest.toolbarEnterClear]
             * # Добавление категории в начало :: [RankTest.toolbarEnterAddStart]
             * # Добавление категории в конец :: [RankTest.toolbarEnterAddEnd]
             */

            /**
             * UI/MANUAL - Управление категорией
             * # Видимость:
             * # Обычное нажатие :: [RankTest.rankVisibleAnimationClick]
             * # Долгое нажатие :: [RankTest.rankVisibleAnimationLongClick]
             * # Заметка :: [RankTest.rankVisibleForNotes]
             * # Заметка в корзине :: [RankTest.rankVisibleForBin]
             *
             * # Удаление:
             * # Удаление из списка :: [RankTest.rankClearFromList]
             * # Заметка :: [RankTest.rankClearForNote]
             *
             * # Перетаскивание
             */

            /**
             * UI - Работа диалога переименовывания
             * # Открыть :: [RankTest.renameDialogOpen]
             *
             * # Закрыть:
             * # Кнопкой назад :: [RankTest.renameDialogCloseSoft]
             * # Кнопкой отмена :: [RankTest.renameDialogCloseCancel]
             *
             * # Блокировка кнопки:
             * # Идентичное название :: [RankTest.renameDialogBlockApplySameName]
             * # Название из списка :: [RankTest.renameDialogBlockApplyFromList]
             *
             * # Результат :: [RankTest.renameDialogResult]
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
             * UI - Открыть настройки :: [NotesTest.openPreference]
             */

            /**
             * CONTROL - Скроллинг списка :: [NotesTest.listScroll]
             */

            /**
             * UI - Видимость кнопки добавить при скроллинге :: [NotesTest.addFabVisibleOnScroll]
             */

            /**
             * UI - Открыть заметку
             * # Текст :: [NotesTest.textNoteOpen]
             * # Список :: [NotesTest.rollNoteOpen]
             */

            /**
             * UI - Создание заметки и возврат назад без сохранения
             * # Текст :: [NotesTest.textNoteCreateAndReturn]
             * # Список :: [NotesTest.rollNoteCreateAndReturn]
             */

            /**
             * UI - Создание заметки и возврат назад с сохранением
             * # Текст :: [NotesTest.textNoteCreateAndReturnWithSave]
             * # Список :: [NotesTest.rollNoteCreateAndReturnWithSave]
             */

            /**
             * UI/CONTROL/MANUAL - Работа диалога управления заметкой
             * # Текст:
             * # Открыть :: [NotesTest.textNoteDialogOpen]
             * # Закрыть :: [NotesTest.textNoteDialogClose]
             * # Прикрепить к статус бару :: [NotesTest.textNoteDialogBind]
             * # Открепить от статус бара :: [NotesTest.textNoteDialogUnbind]
             * # Открепить от статус бара при удалении :: [NotesTest.textNoteDialogUnbindOnDelete]
             * # Конвертировать :: [NotesTest.textNoteDialogConvert]
             * # Копировать текст
             * # Удаление :: [NotesTest.textNoteDialogDelete]
             *
             * # Список:
             * # Открыть :: [NotesTest.rollNoteDialogOpen]
             * # Закрыть :: [NotesTest.rollNoteDialogClose]
             * # Выполнить всё :: [NotesTest.rollNoteDialogCheckAllFromEmpty], [NotesTest.rollNoteDialogCheckAll]
             * # Снять выделения :: [NotesTest.rollNoteDialogUncheckAll]
             * # Прикрепить к статус бару :: [NotesTest.rollNoteDialogBind]
             * # Открепить от статус бара :: [NotesTest.rollNoteDialogUnbind]
             * # Открепить от статус бара при удалении :: [NotesTest.rollNoteDialogUnbind]
             * # Конвертировать :: [NotesTest.rollNoteDialogConvert]
             * # Копировать текст
             * # Удаление :: [NotesTest.rollNoteDialogDelete]
             */

        }

        /**
         * Сценарии для [BinTest]
         */
        class Bin {

            /**
             * UI - Контент
             * # Пусто :: [BinTest.contentEmpty]
             * # Список :: [BinTest.contentList]
             **/

            /**
             * CONTROL - Скроллинг списка :: [BinTest.listScroll]
             */

            /**
             * UI - Открыть заметку
             * # Текст :: [BinTest.textNoteOpen]
             * # Список :: [BinTest.rollNoteOpen]
             */

            /**
             * UI - Работы диалога отчистки корзины :: [BinTest.clearDialogWork]
             * # Открыть :: [BinTest.clearDialogOpen]
             *
             * # Закрыть:
             * # Кнопкой назад :: [BinTest.clearDialogCloseSoft]
             * # Кнопкой нет :: [BinTest.clearDialogCloseCancel]
             *
             * # Результат работы :: [BinTest.clearDialogWork]
             * # Результат работы со скрытыми заметками :: [BinTest.clearDialogWorkWithHideNotes]
             */

            /**
             * UI/MANUAL - Работа диалога управления заметкой
             * # Текст:
             * # Открыть :: [BinTest.textNoteDialogOpen]
             * # Закрыть :: [BinTest.textNoteDialogClose]
             * # Восстановить :: [BinTest.textNoteDialogRestore]
             * # Копировать текст
             * # Очистить :: [BinTest.textNoteDialogClear]
             *
             * # Список:
             * # Открыть :: [BinTest.rollNoteDialogOpen]
             * # Закрыть :: [BinTest.rollNoteDialogClose]
             * # Восстановить :: [BinTest.rollNoteDialogRestore]
             * # Копировать текст
             * # Очистить :: [BinTest.rollNoteDialogClear]
             */

        }


        class TextNote {

            /**
             * Сценарии для [TextNoteToolbarTest]
             */
            class Toolbar {

                /**
                 * UI - Закрытие заметки:
                 * # Только что созданная заметка кнопкой на тулбаре :: [TextNoteToolbarTest.closeByToolbarOnCreate]
                 * # Только что созданная заметка кнопкой назад :: [TextNoteToolbarTest.closeByBackPressOnCreate]
                 * # Открытая заметка кнопкой на тулбаре :: [TextNoteToolbarTest.closeByToolbarOnOpen]
                 * # Открытая заметка кнопкой назад :: [TextNoteToolbarTest.closeByBackPressOnOpen]
                 * # Открытая заметка из корзины кнопкой на тулбаре :: [TextNoteToolbarTest.closeByToolbarOnOpenFromBin]
                 * # Открытая заметка из корзины кнопкой назад :: [TextNoteToolbarTest.closeByBackPressOnOpenFromBin]
                 */

                /**
                 * UI - Отображение:
                 * # Только что созданная заметка :: [TextNoteToolbarTest.contentEmptyOnCreate]
                 * # Открытая заметка без названия :: [TextNoteToolbarTest.contentEmptyOnOpen]
                 * # Открытая заметка с названием :: [TextNoteToolbarTest.contentFillOnOpen]
                 * # Открытая заметка из корзины без названия :: [TextNoteToolbarTest.contentEmptyOnOpenFromBin]
                 * # Открытая заметка из корзины с названием :: [TextNoteToolbarTest.contentFillOnOpenFromBin]
                 * # После восстановления из корзины :: [TextNoteToolbarTest.contentFillOnRestoreOpen]
                 */

                /**
                 * UI - Сохранение:
                 * # При создании заметки :: [TextNoteToolbarTest.saveByControlOnCreate]
                 * # При создании заметки кнопкой назад :: [TextNoteToolbarTest.saveByBackPressOnCreate]
                 * # После редактирования заметки :: [TextNoteToolbarTest.saveByControlOnEdit]
                 * # После редактирования заметки кнопкой назад :: [TextNoteToolbarTest.saveByBackPressOnEdit]
                 */

                /**
                 * UI - Отмена:
                 * # После редактирования заметки :: [TextNoteToolbarTest.cancelOnEditByToolbar]
                 */

            }

            /**
             * Сценарии для [TextNoteContentTest]
             */
            class Content {

                /**
                 * UI - Отображение:
                 * # Только что созданная заметка :: [TextNoteContentTest.contentEmptyOnCreate]
                 * # Открытая заметка :: [TextNoteContentTest.contentFillOnOpen]
                 * # Открытая заметка из корзины :: [TextNoteContentTest.contentFillOnOpenFromBin]
                 * # После восстановления из корзины :: [TextNoteContentTest.contentFillOnRestoreOpen]
                 */

                /**
                 * UI - Сохранение:
                 * # При создании заметки :: [TextNoteContentTest.saveByControlOnCreate]
                 * # При создании заметки кнопкой назад :: [TextNoteContentTest.saveByBackPressOnCreate]
                 * # После редактирования заметки :: [TextNoteContentTest.saveByControlOnEdit]
                 * # После редактирования заметки кнопкой назад :: [TextNoteContentTest.saveByBackPressOnEdit]
                 */

                /**
                 * UI - Отмена:
                 * # После редактирования заметки :: [TextNoteContentTest.cancelOnEditByToolbar]
                 */

            }

            /**
             * Сценарии для [TextNotePanelTest]
             */
            class Panel {

                /**
                 * UI - Отображение:
                 * # Только что созданная заметка :: [TextNotePanelTest.displayOnCreate]
                 * # Открытая заметка :: [TextNotePanelTest.displayOnOpenNote]
                 * # Открытая заметка из корзины :: [TextNotePanelTest.displayOnOpenNoteFromBin]
                 * # После восстановления из корзины :: [TextNotePanelTest.displayOnRestoreOpen]
                 */

                /**
                 * UI - Сохранение
                 * # При создании заметки :: [TextNotePanelTest.saveByControlOnCreate]
                 * # При создании заметки кнопкой назад :: [TextNotePanelTest.saveByPressBackOnCreate]
                 * # После редактирования заметки :: [TextNotePanelTest.saveByControlOnEdit]
                 * # После редактирования заметки кнопкой назад :: [TextNotePanelTest.saveByPressBackOnEdit]
                 */

                /**
                 * UI - Отмена:
                 * # После редактирования заметки :: [TextNotePanelTest.cancelOnEditByToolbar]
                 */

                /**
                 * UI/CONTROL - Функционал
                 * # Корзина:
                 * # Востановление :: [TextNotePanelTest.actionRestoreFromBin]
                 * # Восстановление с открытием :: [TextNotePanelTest.actionRestoreOpenFromBin]
                 * # Удаление на всегда :: [TextNotePanelTest.actionClearFromBin]
                 *
                 * # Созданная/редактируемая заметка:
                 * # Сохранение при создании заметки :: [TextNotePanelTest.actionSaveOnCreate]
                 * # Сохранение после редактирования заметки :: [TextNotePanelTest.actionSaveOnEdit]
                 *
                 * # Открытая заметка:
                 * # Прикрепить к статус бару :: [TextNotePanelTest.actionBindToStatusBar]
                 * # Открепить от статус бара :: [TextNotePanelTest.actionUnbindFromStatusBar]
                 * # Удалить заметку :: [TextNotePanelTest.actionDelete]
                 */

            }

            /**
             * Сценарии для [TextNoteInputTest]
             */
            class Input {

                /**
                 * UI - TODO
                 */

            }

            /**
             * Сценарии для [TextNoteDialogTest]
             */
            class Dialog {

                /**
                 * UI -
                 */

            }

        }

        class RollNote {

            /**
             * Сценарии для [RollNoteToolbarTest]
             */
            class Toolbar {

                /**
                 * UI - Закрытие заметки:
                 * # Только что созданная заметка кнопкой на тулбаре :: [RollNoteToolbarTest.closeByToolbarOnCreate]
                 * # Только что созданная заметка кнопкой назад :: [RollNoteToolbarTest.closeByBackPressOnCreate]
                 * # Открытая заметка кнопкой на тулбаре :: [RollNoteToolbarTest.closeByToolbarOnOpen]
                 * # Открытая заметка кнопкой назад :: [RollNoteToolbarTest.closeByBackPressOnOpen]
                 * # Открытая заметка из корзины кнопкой на тулбаре :: [RollNoteToolbarTest.closeByToolbarOnOpenFromBin]
                 * # Открытая заметка из корзины кнопкой назад :: [RollNoteToolbarTest.closeByBackPressOnOpenFromBin]
                 */

                /**
                 * UI - Отображение:
                 * # Только что созданная заметка :: [RollNoteToolbarTest.contentEmptyOnCreate]
                 * # Открытая заметка без названия :: [RollNoteToolbarTest.contentEmptyOnOpen]
                 * # Открытая заметка с названием :: [RollNoteToolbarTest.contentFillOnOpen]
                 * # Открытая заметка из корзины без названия :: [RollNoteToolbarTest.contentEmptyOnOpenFromBin]
                 * # Открытая заметка из корзины с названием :: [RollNoteToolbarTest.contentFillOnOpenFromBin]
                 * # После восстановления из корзины :: [RollNoteToolbarTest.contentFillOnRestoreOpen]
                 */

                /**
                 * UI - Сохранение:
                 * # При создании заметки :: [RollNoteToolbarTest.saveByControlOnCreate]
                 * # При создании заметки кнопкой назад :: [RollNoteToolbarTest.saveByBackPressOnCreate]
                 * # После редактирования заметки :: [RollNoteToolbarTest.saveByControlOnEdit]
                 * # После редактирования заметки кнопкой назад :: [RollNoteToolbarTest.saveByBackPressOnEdit]
                 */

                /**
                 * UI - Отмена:
                 * # После редактирования заметки :: [RollNoteToolbarTest.cancelOnEditByToolbar]
                 */

            }

            /**
             * Сценарии для [RollNoteContentTest]
             */
            class Content {

                /**
                 * UI - TODO
                 */

            }

            /**
             * Сценарии для [RollNoteEnterTest]
             */
            class Enter {

                /**
                 * UI - TODO
                 */

            }

            /**
             * Сценарии для [RollNotePanelTest]
             */
            class Panel {

                /**
                 * UI - TODO
                 */

            }

            /**
             * Сценарии для [RollNoteInputTest]
             */
            class Input {

                /**
                 * UI - TODO
                 */

            }

            /**
             * Сценарии для [RollNoteDialogTest]
             */
            class Dialog {

                /**
                 * UI -
                 */

            }

        }


        /**
         * TODO
         */
        class Preference

    }

    /**
     * Сценарии для тестов требующих контроля тестировщика
     */
    class Control {

        /**
         * Сценарии для [NoteToolbarIconTest]
         */
        class NoteToolbarIcon {

            /**
             * CONTROL - Стрелка назад у только что созданной заметки
             * # Текст :: [NoteToolbarIconTest.arrowBackOnCreateTextNote]
             * # Список :: [NoteToolbarIconTest.arrowBackOnCreateRollNote]
             */

            /**
             * CONTROL - Не происходит анимации стрелки при сохранении новой заметки
             * # Текст :: [NoteToolbarIconTest.notAnimateOnSaveCreateTextNote]
             * # Список :: [NoteToolbarIconTest.notAnimateOnSaveCreateRollNote]
             */

            /**
             * CONTROL - Не происходит анимации при восстановлении заметки  открытием
             * # Текст :: [NoteToolbarIconTest.notAnimateOnRestoreOpenTextNote]
             * # Список :: [NoteToolbarIconTest.notAnimateOnRestoreOpenRollNote]
             */

            /**
             * CONTROL - Анимация при начале редактирования и сохранении
             * # Текст :: [NoteToolbarIconTest.animateOnEditToSaveTextNote]
             * # Список :: [NoteToolbarIconTest.animateOnEditToSaveRollNote]
             */

            /**
             * CONTROL - Анимация при начале редактирования и отмене
             * # Текст :: [NoteToolbarIconTest.animateOnEditToCancelTextNote]
             * # Список :: [NoteToolbarIconTest.animateOnEditToCancelRollNote]
             */

            /**
             * TODO - Анимация из стрелки в крестик при автоматическом сохранении текста/списка
             */

        }

        /**
         * Сценарии для [RotationTest]
         */
        class Rotation {

            /**
             * Диалог добавления заметки :: [RotationTest.addDialog]
             */

            /**
             * Страница [RankScreen]
             * # Контент
             * # Пусто :: [RotationTest.rankScreenContentEmpty]
             * # Список :: [RotationTest.rankScreenContentList]
             * # Диалог переименовывания с данными :: [RotationTest.rankScreenRenameDialog]
             */

            /**
             * Страница [NotesScreen]
             * # Контент:
             * # Пусто :: [RotationTest.notesScreenContentEmpty]
             * # Список :: [RotationTest.notesScreenContentList]
             *
             * # Диалог управления заметкой:
             * # Текст :: [RotationTest.notesScreenTextNoteDialog]
             * # Список :: [RotationTest.notesScreenRollNoteDialog]
             */

            /**
             * Страница [BinScreen]
             * # Контент:
             * # Пусто :: [RotationTest.binScreenContentEmpty]
             * # Список :: [RotationTest.binScreenContentList]
             *
             * # Диалог отчистки корзины :: [RotationTest.binScreenClearDialog]
             *
             * # Диалог управления заметкой:
             * # Текст :: [RotationTest.binScreenTextNoteDialog]
             * # Список :: [RotationTest.binScreenRollNoteDialog]
             */

            /**
             * Страница [TextNoteScreen] - TODO
             */

            /**
             * Страница [RollNoteScreen] - TODO
             */

            /**
             * После конвертирования
             * # Из текста в список
             * # Из списка в текст
             */

        }

        /**
         * Сценарии для [InfoAnimTest]
         */
        class EmptyInfoAnim {

            /**
             * Страница [RankScreen] :: [InfoAnimTest.rankShowAndHide]
             */

            /**
             * Страница [NotesScreen]
             * # Показать :: [InfoAnimTest.notesShow]
             * # Скрыть :: [InfoAnimTest.notesHide]
             */

            /**
             * Страница [BinScreen]
             * # Показать :: [InfoAnimTest.binShow]
             * # Скрыть :: [InfoAnimTest.binHide]
             */

        }

        /**
         * Сценарии для [BindNoteText]
         */
        class BindNote {

            // TODO проверка обновления ui при откреплении заметки через шторку
            // NotesScreen
            // Text/RollNoteScreen

        }

    }

    class Integration {

        // TODO проверка не удаления скрытых заметок из базы данных

    }

}