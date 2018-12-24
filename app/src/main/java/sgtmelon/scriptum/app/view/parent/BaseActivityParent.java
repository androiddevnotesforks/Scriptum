package sgtmelon.scriptum.app.view.parent;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.def.ThemeDef;
import sgtmelon.scriptum.office.utils.PrefUtils;

public class BaseActivityParent extends AppCompatActivity {

    @ThemeDef private int currentTheme;

    @Override
    protected void onResume() {
        super.onResume();

        isThemeChange();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentTheme = PrefUtils.getTheme(this);
        switch (currentTheme) {
            case ThemeDef.light:
                setTheme(R.style.App_Light_UI);
                break;
            case ThemeDef.dark:
                setTheme(R.style.App_Dark_UI);
                break;
        }
    }

    public final void isThemeChange() {
        if (currentTheme == PrefUtils.getTheme(this)) return;

        final Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        overridePendingTransition(0, 0);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }

}