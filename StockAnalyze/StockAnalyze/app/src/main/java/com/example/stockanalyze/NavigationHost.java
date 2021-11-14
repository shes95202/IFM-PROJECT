package com.example.stockanalyze;

import androidx.fragment.app.Fragment;

public interface NavigationHost {

    void NavigateTo(Fragment fragment,  String tag);

}
