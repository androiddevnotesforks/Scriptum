package sgtmelon.scriptum.test.auto.error

class Description {

    class Note {

        /**
         * Description:
         * ~ If change main model (noteItem) in read mode, then changes not applying for
         *   restore model (restoreItem).
         *
         * Cause:
         * ~ Error occurred due to during read mode, when changes not applying for
         *   restore model (restoreItem).
         *
         * What happening:
         * ~ Changes restored in read mode when in edit mode click on toolbar cancel button. 
         *
         * Important:
         * ~ Need check all changes which user can do in read mode and with
         *   status bar notifications.
         * ~ For status bar note unbind need update only isStatus key. Need test unbind also
         *   in edit mode.
         *
         * Example:
         * ~ Open roll note -> click item check -> click edit -> toolbar cancel.
         * ~ Open text note -> click bind button -> click edit -> toolbar cancel.
         */
        class RestoreChanges

        class Roll {

            /**
             * Description:
             * ~ Check two errors related with each other (must be done in order).
             *
             * 1. In edit mode need remove text from list item and save note (does not matter how).
             *
             * Cause:
             * ~ Due to library DiffUtil. It doesn't know what to do with not saved items
             *   which has equals id.
             *
             * What happening:
             * ~ Item remove slowly.
             * ~ If item in the middle when bug happen.
             *
             * How get:
             * ~ Edit roll note -> remove text from item -> save.
             *
             * 2. Write wrong number of list items (can see in note card indicator on main screen).
             *
             * Cause:
             * ~ Empty list items was removed after calculation of done items.
             *
             * How get:
             * ~ After done of first bug.
             */
            class RemoveEmptyItem

        }

    }

}