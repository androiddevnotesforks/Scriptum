package sgtmelon.scriptum.app.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import sgtmelon.safedialog.library.SingleDialog;
import sgtmelon.safedialog.library.color.ColorDialog;
import sgtmelon.scriptum.BuildConfig;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.factory.DialogFactory;
import sgtmelon.scriptum.app.view.activity.DevelopActivity;
import sgtmelon.scriptum.app.view.activity.PreferenceActivity;
import sgtmelon.scriptum.element.InfoDialog;
import sgtmelon.scriptum.element.SortDialog;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.SortDef;
import sgtmelon.scriptum.office.state.OpenState;
import sgtmelon.scriptum.office.utils.PrefUtils;

public final class PreferenceFragment extends android.preference.PreferenceFragment {

    private static final String TAG = PreferenceFragment.class.getSimpleName();

    private final OpenState openState = new OpenState();

    private PreferenceActivity activity;

    private PrefUtils prefUtils;
    private FragmentManager fm;

    private SortDialog sortDialog;
    private ColorDialog colorDialog;
    private InfoDialog infoDialog;
    private SingleDialog saveTimeDialog;
    private SingleDialog themeDialog;

    private Preference prefSort;
    private String valSort;
    private Preference prefColor;
    private int valColor;
    private Preference prefSaveTime;
    private int valSaveTime;
    private Preference prefTheme;
    private int valTheme;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view != null) {
            final View list = view.findViewById(android.R.id.list);
            list.setPadding(0, 0, 0, 0);
        }

        return view;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        activity = (PreferenceActivity) getActivity();

        prefUtils = new PrefUtils(activity);
        fm = activity.getSupportFragmentManager();

        sortDialog = DialogFactory.INSTANCE.getSortDialog(fm);
        colorDialog = DialogFactory.INSTANCE.getColorDialog(activity, fm);
        infoDialog = DialogFactory.INSTANCE.getInfoDialog(fm);
        saveTimeDialog = DialogFactory.INSTANCE.getSaveTimeDialog(activity, fm);
        themeDialog = DialogFactory.INSTANCE.getThemeDialog(activity, fm);

        if (savedInstanceState != null) {
            openState.setOpen(savedInstanceState.getBoolean(IntentDef.STATE_OPEN));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        setupNotePref();
        setupSavePref();
        setupAppPref();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putBoolean(IntentDef.STATE_OPEN, openState.isOpen());
    }

    private void setupNotePref() {
        Log.i(TAG, "setupNotePref");

        prefSort = findPreference(getString(R.string.pref_key_sort));
        valSort = prefUtils.getSort();
        prefSort.setSummary(prefUtils.getSortSummary(valSort));
        prefSort.setOnPreferenceClickListener(preference -> {
            if (!openState.isOpen()) {
                openState.setOpen(true);

                sortDialog.setArguments(valSort);
                sortDialog.show(fm, DialogDef.SORT);
            }
            return true;
        });

        sortDialog.setPositiveListener((dialogInterface, i) -> {
            valSort = sortDialog.getKeys();
            prefUtils.setSort(valSort);

            final String summary = prefUtils.getSortSummary(valSort);
            prefSort.setSummary(summary);
        });
        sortDialog.setNeutralListener((dialogInterface, i) -> {
            valSort = SortDef.def;
            prefUtils.setSort(valSort);

            final String summary = prefUtils.getSortSummary(valSort);
            prefSort.setSummary(summary);
        });
        sortDialog.setDismissListener(dialogInterface -> openState.setOpen(false));

        prefColor = findPreference(getString(R.string.pref_key_color));
        valColor = prefUtils.getDefaultColor();
        prefColor.setSummary(getResources().getStringArray(R.array.pref_color_text)[valColor]);
        prefColor.setOnPreferenceClickListener(preference -> {
            if (!openState.isOpen()) {
                openState.setOpen(true);

                colorDialog.setArguments(valColor);
                colorDialog.show(fm, DialogDef.COLOR);
            }
            return true;
        });

        colorDialog.setTitle(getString(R.string.pref_color_title));
        colorDialog.setPositiveListener((dialogInterface, i) -> {
            valColor = colorDialog.getCheck();

            prefUtils.setDefaultColor(valColor);
            prefColor.setSummary(getResources().getStringArray(R.array.pref_color_text)[valColor]);
        });
        colorDialog.setDismissListener(dialogInterface -> openState.setOpen(false));
    }

    private void setupSavePref() {
        Log.i(TAG, "setupSavePref");

        prefSaveTime = findPreference(getString(R.string.pref_key_save_time));
        valSaveTime = prefUtils.getSaveTime();
        prefSaveTime.setSummary(getResources().getStringArray(R.array.pref_save_time_text)[valSaveTime]);
        prefSaveTime.setOnPreferenceClickListener(preference -> {
            if (!openState.isOpen()) {
                openState.setOpen(true);

                saveTimeDialog.setArguments(valSaveTime);
                saveTimeDialog.show(fm, DialogDef.SAVE_TIME);
            }
            return true;
        });

        saveTimeDialog.setPositiveListener((dialogInterface, i) -> {
            valSaveTime = saveTimeDialog.getCheck();

            prefUtils.setSaveTime(valSaveTime);
            prefSaveTime.setSummary(saveTimeDialog.getRows()[valSaveTime]);
        });
        saveTimeDialog.setDismissListener(dialogInterface -> openState.setOpen(false));

        final CheckBoxPreference prefAutoSave = (CheckBoxPreference) findPreference(getString(R.string.pref_key_auto_save));
        prefAutoSave.setOnPreferenceChangeListener((preference, newValue) -> {
            prefSaveTime.setEnabled((Boolean) newValue);
            return true;
        });

        prefSaveTime.setEnabled(prefAutoSave.isChecked());
    }

    private void setupAppPref() {
        Log.i(TAG, "setupAppPref");

        prefTheme = findPreference(getString(R.string.pref_key_theme));
        valTheme = prefUtils.getTheme();
        prefTheme.setSummary(getResources().getStringArray(R.array.pref_theme_text)[valTheme]);
        prefTheme.setOnPreferenceClickListener(preference -> {
            if (!openState.isOpen()) {
                openState.setOpen(true);

                themeDialog.setArguments(valTheme);
                themeDialog.show(fm, DialogDef.THEME);
            }
            return true;
        });

        themeDialog.setPositiveListener(((dialogInterface, i) -> {
            valTheme = themeDialog.getCheck();

            prefUtils.setTheme(valTheme);
            prefTheme.setSummary(themeDialog.getRows()[valTheme]);

            activity.isThemeChange();
        }));
        themeDialog.setDismissListener(dialogInterface -> openState.setOpen(false));

        final Preference prefRate = findPreference(getString(R.string.pref_key_rate));
        prefRate.setOnPreferenceClickListener(preference -> {
            final Intent intent = new Intent(Intent.ACTION_VIEW);
            final String packageName = activity.getPackageName();

            try {
                intent.setData(Uri.parse(BuildConfig.MARKET_URL + packageName));
                startActivity(intent);
            } catch (ActivityNotFoundException exception) {
                intent.setData(Uri.parse(BuildConfig.BROWSER_URL + packageName));
                startActivity(intent);
            }
            return true;
        });

        final Preference prefOtherAbout = findPreference(getString(R.string.pref_key_about));
        prefOtherAbout.setOnPreferenceClickListener(preference -> {
            if (!openState.isOpen()) {
                openState.setOpen(true);

                infoDialog.show(fm, DialogDef.INFO);
            }
            return true;
        });

        infoDialog.setLogoClick(view -> {
            final Intent intent = new Intent(activity, DevelopActivity.class);
            startActivity(intent);
        });
        infoDialog.setDismissListener(dialogInterface -> openState.setOpen(false));
    }

}