package com.alp.alpivirzivir.Servis;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class BootStartUpReciever extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        // Start Service On Boot Start Up
        /*
         * public static int TYPE_WIFI = 1;
         * public static int TYPE_MOBILE = 2;
         * public static int TYPE_NOT_CONNECTED = 0;
         * */

        //String status = NetworkUtil.getConnectivityStatusString(context);
        int durum = NetworkUtil.getConnectivityStatus(context);

        //Toast.makeText(context, ""+durum+"",Toast.LENGTH_SHORT).show();

        Intent service = new Intent(context,Servis.class);
        switch(durum){
            case 0:
                //bağlantı yok
                context.stopService(service);
                break;
            default:
                context.startService(service);
                break;
        }
        //Log.d("Açılış","000");


        //uygulamayı başlat
        //Intent App = new Intent(context,SayfaGiris.class);
        //App.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(App);
    }
}
