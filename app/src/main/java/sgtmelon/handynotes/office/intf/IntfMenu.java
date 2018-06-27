package sgtmelon.handynotes.office.intf;

public interface IntfMenu {

    interface MainClick {
        void onMenuNoteClick();
    }

    interface NoteClick {
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

        void onMenuBindClick();

        void onMenuConvertClick();
    }

    interface RollClick {
        void onMenuCheckClick();
    }

    interface DeleteClick {
        void onMenuRestoreClick();

        void onMenuDeleteForeverClick();

        void onMenuDeleteClick();
    }

}
