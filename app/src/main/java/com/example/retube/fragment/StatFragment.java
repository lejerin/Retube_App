package com.example.retube.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.retube.R;
import com.example.retube.Realm.Search;
import com.example.retube.Realm.ViewVideo;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatFragment extends Fragment {

    TextView search_1,search_2,search_3,search_4,search_5,search_6,search_7,search_8,search_9;
    TextView view_1,view_2,view_3,view_4,view_5,view_6,view_7,view_8,view_9;

    Realm realm;
    RealmResults<Search> searches;
    RealmResults<ViewVideo> viewVideos;

    public StatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stat, container, false);

        search_1 = view.findViewById(R.id.search_1);
        search_2 = view.findViewById(R.id.search_2);
        search_3 = view.findViewById(R.id.search_3);
        search_4 = view.findViewById(R.id.search_4);
        search_5 = view.findViewById(R.id.search_5);
        search_6 = view.findViewById(R.id.search_6);
        search_7 = view.findViewById(R.id.search_7);
        search_8 = view.findViewById(R.id.search_8);
        search_9 = view.findViewById(R.id.search_9);

        view_1 = view.findViewById(R.id.view_1);
        view_2 = view.findViewById(R.id.view_2);
        view_3 = view.findViewById(R.id.view_3);
        view_4 = view.findViewById(R.id.view_4);
        view_5 = view.findViewById(R.id.view_5);
        view_6 = view.findViewById(R.id.view_6);
        view_7 = view.findViewById(R.id.view_7);
        view_8 = view.findViewById(R.id.view_8);
        view_9 = view.findViewById(R.id.view_9);

        realm = Realm.getDefaultInstance();
        searches = realm.where(Search.class).sort("count", Sort.ASCENDING).findAll();








        return view;
    }

}
