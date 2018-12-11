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

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import sgtmelon.safedialog.library.ColorDialog;
import sgtmelon.safedialog.library.SingleDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.component.DaggerPreferenceComponent;
import sgtmelon.scriptum.app.injection.component.PreferenceComponent;
import sgtmelon.scriptum.app.injection.module.blank.PreferenceBlankModule;
import sgtmelon.scriptum.app.view.activity.DevelopActivity;
import sgtmelon.scriptum.app.view.activity.PreferenceActivity;
import sgtmelon.scriptum.element.InfoDialog;
import sgtmelon.scriptum.element.SortDialog;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.SortDef;
import sgtmelon.scriptum.office.st.OpenSt;
import sgtmelon.scriptum.office.utils.PrefUtils;

public final class PreferenceFragment extends android.preference.PreferenceFragment {

    private static final String TAG = PreferenceFragment.class.getSimpleName();

    private final OpenSt openSt = new OpenSt();

    @Inject FragmentManager fm;
    @Inject SortDialog sortDialog;
    @Inject ColorDialog colorDialog;
    @Inject InfoDialog infoDialog;

    @Inject
    @Named(DialogDef.SAVE_TIME)
    SingleDialog dlgSaveTime;
    @Inject
    @Named(DialogDef.THEME)
    SingleDialog dlgTheme;

    private PreferenceActivity activity;

    private Preference prefSort;
    private String valSort;
    private Preference prefColor;
    private int valColor;
    private Preference prefSaveTime;
    private int valSaveTime;
    private Preference prefTheme;
    private int valTheme;

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

        final PreferenceComponent preferenceComponent = DaggerPreferenceComponent.builder()
                .preferenceBlankModule(new PreferenceBlankModule(activity))
                .build();
        preferenceComponent.inject(this);

        if (savedInstanceState != null) {
            openSt.setOpen(savedInstanceState.getBoolean(IntentDef.STATE_OPEN));
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

        outState.putBoolean(IntentDef.STATE_OPEN, openSt.isOpen());
    }

    private void setupNotePref() {
        Log.i(TAG, "setupNotePref");

        prefSort = findPreference(getString(R.string.pref_key_sort));
        valSort = PrefUtils.getSort(activity);
        prefSort.setSummary(PrefUtils.getSortSummary(activity, valSort));
        prefSort.setOnPreferenceClickListener(preference -> {
            if (!openSt.isOpen()) {
                openSt.setOpen(true);

                sortDialog.setArguments(valSort);
                sortDialog.show(fm, DialogDef.SORT);
            }
            return true;
        });

        sortDialog.setPositiveListener((dialogInterface, i) -> {
            valSort = sortDialog.getKeys();
            PrefUtils.setSort(activity, valSort);

            final String summary = PrefUtils.getSortSummary(activity, valSort);
            prefSort.setSummary(summary);
        });
        sortDialog.setNeutralListener((dialogInterface, i) -> {
            valSort = SortDef.def;
            PrefUtils.setSort(activity, valSort);

            final String summary = PrefUtils.getSortSummary(activity, valSort);
            prefSort.setSummary(summary);
        });
        sortDialog.setDismissListener(dialogInterface -> openSt.setOpen(false));

        prefColor = findPreference(getString(R.string.pref_key_color));
        valColor = PrefUtils.getDefaultColor(activity);
        prefColor.setSummary(getResources().getStringArray(R.array.pref_color_text)[valColor]);
        prefColor.setOnPreferenceClickListener(preference -> {
            if (!openSt.isOpen()) {
                openSt.setOpen(true);

                colorDialog.setArguments(valColor);
                colorDialog.show(fm, DialogDef.COLOR);
            }
            return true;
        });

        colorDialog.setTitle(getString(R.string.pref_color_title));
        colorDialog.setPositiveListener((dialogInterface, i) -> {
            valColor = colorDialog.getCheck();

            PrefUtils.setDefaultColor(activity, valColor);
            prefColor.setSummary(getResources().getStringArray(R.array.pref_color_text)[valColor]);
        });
        colorDialog.setDismissListener(dialogInterface -> openSt.setOpen(false));
    }

    private void setupSavePref() {
        Log.i(TAG, "setupSavePref");

        prefSaveTime = findPreference(getString(R.string.pref_key_save_time));
        valSaveTime = PrefUtils.getSaveTime(activity);
        prefSaveTime.setSummary(getResources().getStringArray(R.array.pref_save_time_text)[valSaveTime]);
        prefSaveTime.setOnPreferenceClickListener(preference -> {
            if (!openSt.isOpen()) {
                openSt.setOpen(true);

                dlgSaveTime.setArguments(valSaveTime);
                dlgSaveTime.show(fm, DialogDef.SAVE_TIME);
            }
            return true;
        });

        dlgSaveTime.setPositiveListener((dialogInterface, i) -> {
            valSaveTime = dlgSaveTime.getCheck();

            PrefUtils.setSaveTime(activity, valSaveTime);
            prefSaveTime.setSummary(dlgSaveTime.getRows()[valSaveTime]);
        });
        dlgSaveTime.setDismissListener(dialogInterface -> openSt.setOpen(false));

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
        valTheme = PrefUtils.getTheme(activity);
        prefTheme.setSummary(getResources().getStringArray(R.array.pref_theme_text)[valTheme]);
        prefTheme.setOnPreferenceClickListener(preference -> {
            if (!openSt.isOpen()) {
                openSt.setOpen(true);

                dlgTheme.setArguments(valTheme);
                dlgTheme.show(fm, DialogDef.THEME);
            }
            return true;
        });

        dlgTheme.setPositiveListener(((dialogInterface, i) -> {
            valTheme = dlgTheme.getCheck();

            PrefUtils.setTheme(activity, valTheme);
            prefTheme.setSummary(dlgTheme.getRows()[valTheme]);

            activity.isThemeChange();
        }));
        dlgTheme.setDismissListener(dialogInterface -> openSt.setOpen(false));

        Preference prefRate = findPreference(getString(R.string.pref_key_rate));
        prefRate.setOnPreferenceClickListener(preference -> {
            final Intent intent = new Intent(Intent.ACTION_VIEW);
            final String packageName = activity.getPackageName();

            try {
                intent.setData(Uri.parse("market://details?id=" + packageName));
                startActivity(intent);
            } catch (ActivityNotFoundException exception) {
                intent.setData(Uri.parse("https://play.google.com/sore/apps/details?id=" + packageName));
                startActivity(intent);
            }
            return true;
        });

        final Preference prefOtherAbout = findPreference(getString(R.string.pref_key_about));
        prefOtherAbout.setOnPreferenceClickListener(preference -> {
            if (!openSt.isOpen()) {
                openSt.setOpen(true);

                infoDialog.show(fm, DialogDef.INFO);
            }
            return true;
        });

        infoDialog.setLogoClick(view -> {
            final Intent intent = new Intent(activity, DevelopActivity.class);
            startActivity(intent);
        });
        infoDialog.setDismissListener(dialogInterface -> openSt.setOpen(false));
    }

}