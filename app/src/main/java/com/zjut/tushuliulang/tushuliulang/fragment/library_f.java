package com.zjut.tushuliulang.tushuliulang.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjut.tushuliulang.tushuliulang.MainActivity;
import com.zjut.tushuliulang.tushuliulang.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class library_f extends Fragment {


    //侧滑栏
    private static final String ARG_SECTION_NUMBER = "section_number";

    //侧滑栏
    public static Fragment newInstance(int sectionNumber) {
        share_f fragment = new share_f();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    //侧滑栏
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    //侧滑栏
    public library_f() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library_f, container, false);
    }


}
