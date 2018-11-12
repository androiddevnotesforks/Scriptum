package sgtmelon.scriptum.app.view.parent;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.HelpUtils;
import sgtmelon.scriptum.office.annot.def.ThemeDef;

public class BaseActivityParent extends AppCompatActivity {

    private int valTheme;

    @Override
    protected void onResume() {
        super.onResume();
        isThemeChange();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        valTheme = HelpUtils.Pref.getTheme(this);
        switch (valTheme) {
            case ThemeDef.light:
                setTheme(R.style.App_Light_UI);
                break;
            case ThemeDef.dark:
                setTheme(R.style.App_Dark_UI);
                break;
        }
    }

    public final void isThemeChange() {
        int valTheme = HelpUtils.Pref.getTheme(this);
        if (this.valTheme != valTheme) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

}
