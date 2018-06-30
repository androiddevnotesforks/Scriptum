package sgtmelon.handynotes.office.st;

public class StCheck {

    private boolean all;

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public boolean setAll(int checkValue, int listSize) {
        boolean all = checkValue == listSize;

        if (this.all != all) {
            this.all = all;
            return true;
        }
        return false;
    }

}
