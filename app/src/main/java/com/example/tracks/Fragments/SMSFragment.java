package com.example.tracks.Fragments;

import com.example.tracks.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class SMSFragment extends Fragment {

    EditText etPhoneNumberMain;
    EditText etMsgBosyMain;
    Button btnSendSMSMain;

    public SMSFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_s_m_s, container, false);

        // ربط العناصر
        etPhoneNumberMain = view.findViewById(R.id.etPhoneNumberMain);
        etMsgBosyMain = view.findViewById(R.id.etMsgBosyMain);
        btnSendSMSMain = view.findViewById(R.id.btnSendSMSMain);

        btnSendSMSMain.setOnClickListener(v -> {

            String phoneNumber = etPhoneNumberMain.getText().toString();
            String message = etMsgBosyMain.getText().toString();

            if (!phoneNumber.isEmpty() && !message.isEmpty()) {

                // الطريقة الأولى (أفضل)
                sendSmsWithIntent(phoneNumber, message);

                // الطريقة الثانية (تحتاج صلاحيات)
                /*
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                */

            } else {
                Toast.makeText(getContext(), "Some fields are empty", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void sendSmsWithIntent(String phoneNumber, String message) {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:" + phoneNumber));
        smsIntent.putExtra("sms_body", message);

        try {
            startActivity(smsIntent);
        } catch (Exception ex) {
            Toast.makeText(getContext(), "SMS failed", Toast.LENGTH_SHORT).show();
        }
    }
}