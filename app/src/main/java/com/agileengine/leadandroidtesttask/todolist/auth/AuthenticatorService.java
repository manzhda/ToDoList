package com.agileengine.leadandroidtesttask.todolist.auth;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * A bound Service that instantiates the authenticator
 * when started.
 */
public class AuthenticatorService extends Service {

    public static final String ACCOUNT_NAME = "sync";

    // Instance field that stores the authenticator object
    private Authenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new Authenticator(this);
    }

    /*
     * return stub's account for SyncAdapter
     */
    public static Account getAccount() {
        return new Account(ACCOUNT_NAME, getAccountType());
    }

    protected static String getAccountType(){
        return "";
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}