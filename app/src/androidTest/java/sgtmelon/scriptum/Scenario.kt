package sgtmelon.scriptum

import sgtmelon.scriptum.test.auto.IntroTest
import sgtmelon.scriptum.test.auto.NotificationTest
import sgtmelon.scriptum.test.auto.SplashTest
import sgtmelon.scriptum.test.auto.main.BinTest
import sgtmelon.scriptum.test.auto.main.MainTest
import sgtmelon.scriptum.test.auto.main.NotesTest
import sgtmelon.scriptum.test.auto.main.RankTest
import sgtmelon.scriptum.test.auto.note.RollNoteTest
import sgtmelon.scriptum.test.auto.note.TextNoteTest
import sgtmelon.scriptum.test.auto.note.roll.RollNoteToolbarTest
import sgtmelon.scriptum.test.auto.note.text.TextNoteContentTest
import sgtmelon.scriptum.test.auto.note.text.TextNotePanelTest
import sgtmelon.scriptum.test.auto.note.text.TextNoteToolbarTest
import sgtmelon.scriptum.test.control.InfoAnimTest
import sgtmelon.scriptum.test.control.NoteToolbarIconTest
import sgtmelon.scriptum.test.control.RotationTest
import sgtmelon.scriptum.test.control.alarm.AlarmAnimDarkTest
import sgtmelon.scriptum.test.control.alarm.AlarmAnimLightTest
import sgtmelon.scriptum.ui.screen.NotificationScreen
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
private class Scenario {

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

        // TODO после результата переименовывания диалога обновлять тулбар

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
             * Открыть приветствие :: [SplashTest.introScreenOpen]
             */

            /**
             * Открыть главный экран :: [SplashTest.mainScreenOpen]
             */

            /**
             * Открыть заметку через уведомление
             * # Текст :: [SplashTest.bindTextNoteOpen]
             * # Список :: [SplashTest.bindRollNoteOpen]
             */

            /**
             * Открыть уведомление по времени
             * # Текст :: [SplashTest.alarmTextNoteOpen]
             * # Список :: [SplashTest.alarmRollNoteOpen]
             */

        }

        /**
         * Сценарии для [IntroTest]
         */
        class Intro {

            /**
             * Расположение контента на страницах :: [IntroTest.contentPlacement]
             */

            /**
             * Работа кнопки end :: [IntroTest.endButtonWork]
             */

        }

        /**
         * Сценарии для [MainTest]
         */
        class Main {

            /**
             * Стартовый экран :: [MainTest.startScreen]
             */

            /**
             * Отображение правильного экрана при нажатии на пункт меню :: [MainTest.menuClickCorrectScreen]
             */

            /**
             * Отображения кнопки добавить :: [MainTest.addFabVisible]
             */

            /**
             * Работа диалога добавления заметки
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
             * Скроллинга страниц до верха
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
             * Контент
             * # Пусто :: [RankTest.contentEmpty]
             * # Список :: [RankTest.contentList]
             **/

            /**
             * Скроллинг списка :: [RankTest.listScroll]
             */

            /**
             * Работа toolbar'а добавления категории
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
             * Управление категорией
             * # Видимость:
             * # Обычное нажатие :: [RankTest.rankVisibleAnimationClick]
             * # Долгое нажатие :: [RankTest.rankVisibleAnimationLongClick]
             * # Заметка :: [RankTest.rankVisibleForNotes]
             * # Заметка в корзине :: [RankTest.rankVisibleForBin]
             *
             * # Удаление:
             * # Удаление из списка :: [RankTest.rankClearFromList]
             * # Для заметок :: [RankTest.rankClearForNote]
             * # Для корзины :: [RankTest.rankClearForBin]
             *
             * # Перетаскивание
             */

            /**
             * Работа диалога переименовывания
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
             * Контент
             * # Пусто :: [NotesTest.contentEmpty]
             * # Список :: [NotesTest.contentList]
             **/

            /**
             * Открыть настройки :: [NotesTest.openPreference]
             */

            /**
             * Скроллинг списка :: [NotesTest.listScroll]
             */

            /**
             * Видимость кнопки добавить при скроллинге :: [NotesTest.addFabVisibleOnScroll]
             */

            /**
             * Открыть заметку
             * # Текст :: [NotesTest.textNoteOpen]
             * # Список :: [NotesTest.rollNoteOpen]
             */

            /**
             * Создание заметки и возврат назад без сохранения
             * # Текст :: [NotesTest.textNoteCreateAndReturn]
             * # Список :: [NotesTest.rollNoteCreateAndReturn]
             */

            /**
             * Создание заметки и возврат назад с сохранением
             * # Текст :: [NotesTest.textNoteCreateAndReturnWithSave]
             * # Список :: [NotesTest.rollNoteCreateAndReturnWithSave]
             */

            /**
             * Работа диалога управления заметкой
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
             * Контент
             * # Пусто :: [BinTest.contentEmpty]
             * # Список :: [BinTest.contentList]
             **/

            /**
             * Скроллинг списка :: [BinTest.listScroll]
             */

            /**
             * Открыть заметку
             * # Текст :: [BinTest.textNoteOpen]
             * # Список :: [BinTest.rollNoteOpen]
             */

            /**
             * Работы диалога отчистки корзины :: [BinTest.clearDialogWork]
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
             * Работа диалога управления заметкой
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

        /**
         * Сценарии для [TextNoteTest]
         */
        class TextNote {

            /**
             * Заметка в корзине
             * # Контент:
             * # Открытие заметки без названия :: [TextNoteTest.binContentWithoutName]
             * # Открытие заметки с названием :: [TextNoteTest.binContentWithName]
             *
             * # Выход из заметки :: [TextNoteTest.binClose]
             *
             * # Панель:
             * # Востановление :: [TextNoteTest.binActionRestore]
             * # Восстановление с открытием :: [TextNoteTest.binActionRestoreOpen]
             * # Удаление навсегда :: [TextNoteTest.binActionClear]
             */

            /**
             * Сценарии для [TextNoteToolbarTest]
             */
            class Toolbar {

                /**
                 * Закрытие заметки:
                 * # Только что созданная заметка кнопкой на тулбаре :: [TextNoteToolbarTest.closeByToolbarOnCreate]
                 * # Только что созданная заметка кнопкой назад :: [TextNoteToolbarTest.closeByBackPressOnCreate]
                 * # Открытая заметка кнопкой на тулбаре :: [TextNoteToolbarTest.closeByToolbarOnOpen]
                 * # Открытая заметка кнопкой назад :: [TextNoteToolbarTest.closeByBackPressOnOpen]
                 */

                /**
                 * Отображение:
                 * # Только что созданная заметка :: [TextNoteToolbarTest.contentEmptyOnCreate]
                 * # Открытая заметка без названия :: [TextNoteToolbarTest.contentEmptyOnOpen]
                 * # Открытая заметка с названием :: [TextNoteToolbarTest.contentFillOnOpen]
                 */

                /**
                 * Сохранение:
                 * # При создании заметки :: [TextNoteToolbarTest.saveByControlOnCreate]
                 * # При создании заметки кнопкой назад :: [TextNoteToolbarTest.saveByBackPressOnCreate]
                 * # После редактирования заметки :: [TextNoteToolbarTest.saveByControlOnEdit]
                 * # После редактирования заметки кнопкой назад :: [TextNoteToolbarTest.saveByBackPressOnEdit]
                 */

                /**
                 * Отмена:
                 * # После редактирования заметки :: [TextNoteToolbarTest.cancelOnEditByToolbar]
                 */

            }

            /**
             * Сценарии для [TextNoteContentTest]
             */
            class Content {

                /**
                 * Отображение:
                 * # Только что созданная заметка :: [TextNoteContentTest.contentEmptyOnCreate]
                 * # Открытая заметка :: [TextNoteContentTest.contentFillOnOpen]
                 */

                /**
                 * Сохранение:
                 * # При создании заметки :: [TextNoteContentTest.saveByControlOnCreate]
                 * # При создании заметки кнопкой назад :: [TextNoteContentTest.saveByBackPressOnCreate]
                 * # После редактирования заметки :: [TextNoteContentTest.saveByControlOnEdit]
                 * # После редактирования заметки кнопкой назад :: [TextNoteContentTest.saveByBackPressOnEdit]
                 */

                /**
                 * Отмена:
                 * # После редактирования заметки :: [TextNoteContentTest.cancelOnEditByToolbar]
                 */

            }

            /**
             * Сценарии для [TextNotePanelTest]
             */
            class Panel {

                /**
                 * Отображение:
                 * # Только что созданная заметка :: [TextNotePanelTest.displayOnCreate]
                 * # Открытая заметка :: [TextNotePanelTest.displayOnOpenNote]
                 */

                /**
                 * Сохранение
                 * # При создании заметки :: [TextNotePanelTest.saveByControlOnCreate]
                 * # При создании заметки кнопкой назад :: [TextNotePanelTest.saveByPressBackOnCreate]
                 * # После редактирования заметки :: [TextNotePanelTest.saveByControlOnEdit]
                 * # После редактирования заметки кнопкой назад :: [TextNotePanelTest.saveByPressBackOnEdit]
                 */

                /**
                 * Отмена:
                 * # После редактирования заметки :: [TextNotePanelTest.cancelOnEditByToolbar]
                 */

                /**
                 * UI/Функционал
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

        }

        /**
         * Сценарии для [RollNoteTest]
         */
        class RollNote {

            /**
             * Заметка в корзине
             * # Контент:
             * # Открытие заметки без названия :: [RollNoteTest.binContentWithoutName]
             * # Открытие заметки с названием :: [RollNoteTest.binContentWithName]
             *
             * # Выход из заметки :: [RollNoteTest.binClose]
             *
             * # Панель:
             * # Востановление :: [RollNoteTest.binActionRestore]
             * # Восстановление с открытием :: [RollNoteTest.binActionRestoreOpen]
             * # Удаление навсегда :: [RollNoteTest.binActionClear]
             */


            /**
             * Сценарии для [RollNoteToolbarTest]
             */
            class Toolbar {

                /**
                 * Закрытие заметки:
                 * # Только что созданная заметка кнопкой на тулбаре :: [RollNoteToolbarTest.closeByToolbarOnCreate]
                 * # Только что созданная заметка кнопкой назад :: [RollNoteToolbarTest.closeByBackPressOnCreate]
                 * # Открытая заметка кнопкой на тулбаре :: [RollNoteToolbarTest.closeByToolbarOnOpen]
                 * # Открытая заметка кнопкой назад :: [RollNoteToolbarTest.closeByBackPressOnOpen]
                 */

                /**
                 * Отображение:
                 * # Только что созданная заметка :: [RollNoteToolbarTest.contentEmptyOnCreate]
                 * # Открытая заметка без названия :: [RollNoteToolbarTest.contentEmptyOnOpen]
                 * # Открытая заметка с названием :: [RollNoteToolbarTest.contentFillOnOpen]
                 */

                /**
                 * Сохранение:
                 * # При создании заметки :: [RollNoteToolbarTest.saveByControlOnCreate]
                 * # При создании заметки кнопкой назад :: [RollNoteToolbarTest.saveByBackPressOnCreate]
                 * # После редактирования заметки :: [RollNoteToolbarTest.saveByControlOnEdit]
                 * # После редактирования заметки кнопкой назад :: [RollNoteToolbarTest.saveByBackPressOnEdit]
                 */

                /**
                 * Отмена:
                 * # После редактирования заметки :: [RollNoteToolbarTest.cancelOnEdit]
                 */

            }

        }

        /**
         * Сценарии для [NotificationTest]
         */
        class Notification {

            /**
             * Контент
             * # Пусто :: [NotificationTest.contentEmpty]
             * # Список :: [NotificationTest.contentList]
             **/

            /**
             * Скроллинг списка :: [NotificationTest.listScroll]
             */

            /**
             * Удаление элементов :: [NotificationTest.itemCancel]
             */

            /**
             * Открыть уведомление
             * # Текст :: [NotificationTest.textNoteOpen]
             * # Список :: [NotificationTest.rollNoteOpen]
             */

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
             * Стрелка назад у только что созданной заметки
             * # Текст :: [NoteToolbarIconTest.arrowBackOnCreateTextNote]
             * # Список :: [NoteToolbarIconTest.arrowBackOnCreateRollNote]
             */

            /**
             * Не происходит анимации стрелки при сохранении новой заметки
             * # Текст :: [NoteToolbarIconTest.notAnimateOnSaveCreateTextNote]
             * # Список :: [NoteToolbarIconTest.notAnimateOnSaveCreateRollNote]
             */

            /**
             * Не происходит анимации при восстановлении заметки  открытием
             * # Текст :: [NoteToolbarIconTest.notAnimateOnRestoreOpenTextNote]
             * # Список :: [NoteToolbarIconTest.notAnimateOnRestoreOpenRollNote]
             */

            /**
             * Анимация при начале редактирования и сохранении
             * # Текст :: [NoteToolbarIconTest.animateOnEditToSaveTextNote]
             * # Список :: [NoteToolbarIconTest.animateOnEditToSaveRollNote]
             */

            /**
             * Анимация при начале редактирования и отмене
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
             * Страница [NotificationScreen]
             * # Контент:
             * # Пусто :: [RotationTest.notificationScreenContentEmpty]
             * # Список :: [RotationTest.notificationScreenContentList]
             */


            // TODO --------------------------------------------------------------------------------

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
        class InfoAnim {

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

            /**
             * Страница [NotificationScreen] ::  [InfoAnimTest.notificationShow]
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

        /**
         * Сценарии для [AlarmAnimLightTest] и [AlarmAnimDarkTest]
         */
        class AlarmAnim {

            /**
             * Светлая тема
             * Красный :: [AlarmAnimLightTest.colorRed]
             * Фиолетовый :: [AlarmAnimLightTest.colorPurple]
             * Индиго :: [AlarmAnimLightTest.colorIndigo]
             * Синий :: [AlarmAnimLightTest.colorBlue]
             * Бирюзовый :: [AlarmAnimLightTest.colorTeal]
             * Зелёный :: [AlarmAnimLightTest.colorGreen]
             * Желтый :: [AlarmAnimLightTest.colorYellow]
             * Оранжевый :: [AlarmAnimLightTest.colorOrange]
             * Коричневый :: [AlarmAnimLightTest.colorBrown]
             * Сине-серый :: [AlarmAnimLightTest.colorBlueGrey]
             * Белый :: [AlarmAnimLightTest.colorWhite]
             */

            /**
             * Тёмная тема
             * Красный :: [AlarmAnimDarkTest.colorRed]
             * Фиолетовый :: [AlarmAnimDarkTest.colorPurple]
             * Индиго :: [AlarmAnimDarkTest.colorIndigo]
             * Синий :: [AlarmAnimDarkTest.colorBlue]
             * Бирюзовый :: [AlarmAnimDarkTest.colorTeal]
             * Зелёный :: [AlarmAnimDarkTest.colorGreen]
             * Желтый :: [AlarmAnimDarkTest.colorYellow]
             * Оранжевый :: [AlarmAnimDarkTest.colorOrange]
             * Коричневый :: [AlarmAnimDarkTest.colorBrown]
             * Сине-серый :: [AlarmAnimDarkTest.colorBlueGrey]
             * Белый :: [AlarmAnimDarkTest.colorWhite]
             */

        }

    }

    class Integration {

        // TODO проверка не удаления скрытых заметок из базы данных
        // TODO сценарий проверки правильности получения данных при отсутствии их в бд (удалил alarmItem, а noteModel пытается получить её и NPE commit 18.06.19 (62ecee45))

    }

}