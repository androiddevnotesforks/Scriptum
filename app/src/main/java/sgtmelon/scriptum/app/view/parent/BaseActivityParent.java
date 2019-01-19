package sgtmelon.scriptum.app.view.parent;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentTheme = PrefUtils.getInstance(this).getTheme();
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
        if (currentTheme == PrefUtils.getInstance(this).getTheme()) return;

        final Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();

        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        startActivity(intent);
    }

}