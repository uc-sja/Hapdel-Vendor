package com.utility.hapdelvendor;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.utility.hapdelvendor.Interfaces.LocationMethods;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BottomSheetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomSheetFragment extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public EditText search_bar;
    public LinearLayout current_location_layout, select_address_layout;
    private int AUTOCOMPLETE_REQUEST_CODE = 1121;
    private static final String TAG = "BottomSheetFragment";
    public Activity activity;
    public LocationMethods locationMethods;


    public BottomSheetFragment(LocationMethods locationMethods) {
        // Required empty public constructor
        this.activity = activity;
        this.locationMethods = locationMethods;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if(!Places.isInitialized()){
            Places.initialize(getContext(), getString(R.string.place_api_key));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        return view;

    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: ");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getContext());

        search_bar = view.findViewById(R.id.search_bar);
        search_bar.requestFocus();

        current_location_layout = view.findViewById(R.id.current_location_layout);
        select_address_layout = view.findViewById(R.id.select_address_layout);

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                locationMethods.onSearchCalled();
            }
        });

        current_location_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                locationMethods.fetchCurrentLocation();
            }
        });


        select_address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                locationMethods.showAddressList();

            }
        });

    }
}


