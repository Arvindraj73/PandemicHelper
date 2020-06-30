package com.mcet.pandemichelper;

import com.google.firebase.messaging.FirebaseMessagingService;

import androidx.annotation.NonNull;

public class DeviceTokenService extends FirebaseMessagingService {

    private String token;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        token = s;
    }

    public String getToken() {
        return token;
    }

}
