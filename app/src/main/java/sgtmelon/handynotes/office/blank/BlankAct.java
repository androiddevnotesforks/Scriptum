package sgtmelon.handynotes.office.blank;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.def.DefTheme;

public class BlankAct extends AppCompatActivity {

    private int valTheme;

    @Override
    protected void onResume() {
        super.onResume();

        isThemeChange();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        valTheme = Help.Pref.getTheme(this);
        switch (valTheme) {
            case DefTheme.light:
                setTheme(R.style.App_Light_UI);
                break;
            case DefTheme.dark:
                setTheme(R.style.App_Dark_UI);
                break;
        }
    }

    public void isThemeChange() {
        int valTheme = Help.Pref.getTheme(this);
        if (this.valTheme != valTheme) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

}
