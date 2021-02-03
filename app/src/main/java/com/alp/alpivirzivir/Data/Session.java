package com.alp.alpivirzivir.Data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorSpace;

import com.alp.alpivirzivir.Business.Ayar;
import com.alp.alpivirzivir.Model.Login;
import com.alp.alpivirzivir.SayfaGiris;
import com.alp.alpivirzivir.SayfaLogin;

import java.util.HashMap;

public class Session {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "Alptekin Bodur";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_ISIM = "isim";
    public static final String KEY_ID = "id";
    public static final String KEY_RESIM="resim";
    public Session(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    //public void createLoginSession(String strID,String strIsim,String strResim){
    public void createLoginSession(Login lg){
        // fonksiyonu güncelle
        Ayar.UyeGiris = true;
        Ayar.UyeAdi = lg.getIsim();
        Ayar.UyeID = String.valueOf(lg.getId());
        Ayar.UyeResim = lg.getResim();
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, Ayar.UyeID);
        editor.putString(KEY_ISIM, Ayar.UyeAdi);
        editor.putString(KEY_RESIM, Ayar.UyeResim);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, SayfaLogin.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> u = new HashMap<String, String>();
        u.put(KEY_ID, pref.getString(KEY_ID, null));
        u.put(KEY_ISIM, pref.getString(KEY_ISIM, null));
        u.put(KEY_RESIM, pref.getString(KEY_RESIM, null));
        return u;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Ayar.UyeGiris=false;
        Intent i = new Intent(_context, SayfaGiris.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    // güncelle
    public void update(Login lg){
        editor.clear();
        editor.commit();
        createLoginSession(lg);
    }
}
