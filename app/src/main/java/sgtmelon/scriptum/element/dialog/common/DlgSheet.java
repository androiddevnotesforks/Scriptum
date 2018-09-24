package sgtmelon.scriptum.element.dialog.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

public class DlgSheet extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    // TODO: 24.09.2018 ошибка при тсутствии слоя

    @LayoutRes
    private int layout;

    @IdRes
    private int navigation;

    public void setLayout(@LayoutRes int layout) {
        this.layout = layout;
    }

    public void setNavigation(@IdRes int navigation) {
        this.navigation = navigation;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout, container, false);

        NavigationView navigationView = view.findViewById(navigation);
        navigationView.setNavigationItemSelectedListener(this);

        return view;
    }

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener;

    public void setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener) {
        this.navigationItemSelectedListener = navigationItemSelectedListener;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        navigationItemSelectedListener.onNavigationItemSelected(menuItem);
        return false;
    }
}
