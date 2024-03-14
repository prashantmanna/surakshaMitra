package com.surakshamitra;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.surakshamitra.database.messageSaved;

import java.util.ArrayList;

public class chatfragment extends Fragment {

    private ArrayList<String> arrChat = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private messageSaved dbHelper;
    private EditText chatInput;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatfragment, container, false);

        ListView listView = view.findViewById(R.id.listChat);
        dbHelper = new messageSaved(requireContext());
        chatInput = view.findViewById(R.id.messageSMS);

        loadMessages();

        adapter = new ArrayAdapter<String>(requireContext(), R.layout.customchat, arrChat) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.customchat, parent, false);
                }

                final String message = getItem(position);

                TextView textMessage = convertView.findViewById(R.id.customChat1);
                textMessage.setText(message);

                textMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle click on the custom TextView
                        sendSms("phoneNumber", message);
                    }
                });

                return convertView;
            }
        };

        listView.setAdapter(adapter);

        // Floating Action Button click listener
        FloatingActionButton sendButton = view.findViewById(R.id.fy);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessage();
            }
        });

        return view;
    }

    private void loadMessages() {
        arrChat.clear();
        arrChat.addAll(dbHelper.getAllMessages());
    }

    private void saveMessage() {
        String message = chatInput.getText().toString().trim();

        if (!message.isEmpty()) {

            if (dbHelper.addMessage(message)) {
                chatInput.getText().clear();
                loadMessages();
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "Failed to save message", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSms(String phoneNumber, String message) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(requireContext(), "SMS sent successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to send SMS", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where SMS permission is not granted
            Toast.makeText(requireContext(), "SMS permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_REQUEST_CODE);
        }
    }
}
