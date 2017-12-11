package com.kartikeymishr.batch;

/**
 * Created by karti on 11-12-2017.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

public class MailTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_mail, container, false);
        return rootView;
    }
}