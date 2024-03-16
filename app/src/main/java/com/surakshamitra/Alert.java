package com.surakshamitra;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.surakshamitra.DBhelper;
import com.surakshamitra.messageSaved;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlinx.coroutines.channels.ChannelResult;

public class Alert extends Fragment {

    static final int PERMISSION_REQUEST_CODE_SMS = 1000;
    ImageView sos;
    DBhelper dBhelper;

    CircleImageView img2;

    private static final String PERMISSION_SMS = Manifest.permission.SEND_SMS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert, container, false);
        sos = view.findViewById(R.id.sos);
        dBhelper = new DBhelper(requireContext());


        sos.setOnClickListener(new View.OnClickListener() {
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
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),PERMISSION_SMS)) {
            AlertDialog.Builder dialog =  new AlertDialog.Builder(requireActivity());
            dialog.setMessage("This app requires SEND_SMS permission for particular feature")
                    .setTitle("PERMISSION REQUIRED")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.SEND_SMS},PERMISSION_REQUEST_CODE_SMS);
                            dialog.dismiss();
                        }
                    })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
            dialog.show();

            
        }else
        {
            ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.SEND_SMS},PERMISSION_REQUEST_CODE_SMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_SMS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendAlertSMS();
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.SEND_SMS)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setMessage("This app requires permission to send sms" + "Please allow permission to send sms")
                    .setTitle("Permission Required")
                    .setCancelable(false)
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",requireContext().getPackageName(),null);
                            intent.setData(uri);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
            builder.show();


        }
        else {
            checkAndRequestPermissions();
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
            String message = "Alert! This is an emergency message from prashant";
            smsManager.sendTextMessage(number, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
