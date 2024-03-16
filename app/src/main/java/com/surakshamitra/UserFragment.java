package com.surakshamitra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment {

    private String contactName;
    private String contactInfo;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String contactName, String contactInfo) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString("contactName", contactName);
        args.putString("contactInfo", contactInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contactName = getArguments().getString("contactName");
            contactInfo = getArguments().getString("contactInfo");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewInfo = view.findViewById(R.id.textViewInfo);

        textViewName.setText(contactName);
        textViewInfo.setText(contactInfo);

        return view;
    }
}
