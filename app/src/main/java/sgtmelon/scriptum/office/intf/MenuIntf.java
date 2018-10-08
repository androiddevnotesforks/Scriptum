package sgtmelon.scriptum.office.intf;

public interface MenuIntf {

    /**
     * Интерфейс для меню внутри заметки
     */
    interface Note {

        interface DeleteMenuClick {
            void onMenuRestoreClick();

            void onMenuRestoreOpenClick();

            void onMenuClearClick();

            void onMenuDeleteClick();
        }

        interface NoteMenuClick {
            /**
             * @param editModeChange - Надо ли менять режим редактирования
             * @return - True - сохранение успешно
             */
            boolean onMenuSaveClick(boolean editModeChange);

            void onMenuRankClick();

            void onMenuColorClick();

            /**
             * @param editMode - Установка режима редактирования
             */
            void onMenuEditClick(boolean editMode);

            void onMenuCheckClick();

            void onMenuBindClick();

            void onMenuConvertClick();
        }

    }

    /**
     * Интерфейс для меню при долгом нажатии на заметку
     */
    interface Dialog {

        interface DeleteMenuClick {
            void onMenuRestoreClick(int p);

            void onMenuCopyClick(int p);

            void onMenuClearClick(int p);
        }

        interface NoteMenuClick {
            void onMenuCheckClick(int p);

            void onMenuBindClick(int p);

            void onMenuConvertClick(int p);

            void onMenuCopyClick(int p);

            void onMenuDeleteClick(int p);
        }

    }

}
