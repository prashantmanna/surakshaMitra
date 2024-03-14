package com.surakshamitra;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.surakshamitra.database.DBhelper;
import com.surakshamitra.database.messageSaved;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlinx.coroutines.channels.ChannelResult;

public class Alert extends Fragment {

    static final int PERMISSION_REQUEST_CODE = 100;
    ImageView img1;
    DBhelper dBhelper;

    CircleImageView img2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert, container, false);
        img1 = view.findViewById(R.id.sos);
        dBhelper = new DBhelper(requireContext());
        img2 = view.findViewById(R.id.editSMS);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermissions();
            }
        });


        return view;
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sendAlertSMS();
        } else {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendAlertSMS();
        } else {
            Toast.makeText(requireContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendAlertSMS() {
        ArrayList<model> contactsList = dBhelper.getAllContactsForSMS();

        for (model contact : contactsList) {
            String name = contact.getContact();
            String number = contact.getNumber();

            sendSMS(name, number);
        }

        Toast.makeText(requireContext(), "SMS sent to all contacts", Toast.LENGTH_SHORT).show();
    }

    private void sendSMS(String name, String number) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String message = "Alert! This is an emergency message from " + name;
            smsManager.sendTextMessage(number, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
