package com.alp.alpivirzivir.Servis;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.alp.alpivirzivir.Business.Ayar;
import com.alp.alpivirzivir.Business.Guvenlik;
import com.alp.alpivirzivir.Business.Islev;
import com.alp.alpivirzivir.Business.JsonParser;
import com.alp.alpivirzivir.Data.Veri;
import com.alp.alpivirzivir.R;
import com.alp.alpivirzivir.SayfaGiris;
import com.alp.alpivirzivir.Socket.WebSocketClient;

public class Servis extends Service{
    private static final String TAG = "Servis";
    public static final String BROADCAST_ACTION = "com.alp.alpivirzivir.Servis.displayevent";
    private final Handler handler = new Handler();
    Intent intent;

    //soket
    WebSocketClient client;
    String strGelenMesaj;
    List<BasicNameValuePair> extraHeaders;

    //Tracker gps;
    Veri db;
    JSONArray jsonSiparisler=null;
    int iSiparisToplam=0;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        Log.d(TAG,"Servis oluşturuldu");

        db=new Veri(this);

        //gps başlat durdur
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverGpsBaslat, new IntentFilter("GpsTakip"));
        //değişen koordinatları gönder
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverGps, new IntentFilter("KoordinatDegisti"));
        //sipariş durum değişti
        LocalBroadcastManager.getInstance(this).registerReceiver(mRecieverSip, new IntentFilter("SiparisDurumDegisti"));


    }

    @Override
    public void onStart(Intent intent, int startId) {
        SoketBaslat();

        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
        Log.d(TAG, "Servis başlatıldı");


    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 1000 * 60 * 30); // 30 dakika
        }
    };

    private void DisplayLoggingInfo() {
        //Log.d(TAG, "entered DisplayLoggingInfo counter("+ String.valueOf(++counter) +") ");
        //intent.putExtra("time", new Date().toLocaleString());
        //intent.putExtra("counter", String.valueOf(++counter));
        //sendBroadcast(intent);
        try{
            client.send("0");
        }catch(Exception e){
            //hata
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"Servis durduruldu");
        Ayar.UygulamaServis=false;
        extraHeaders=null;
        try{
            client.disconnect();
        }catch(Exception e){}
        handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
    }



    // soket işlemler
    private void SoketBaslat(){
        try{
            client.disconnect();
        }catch(Exception e){
            //Log.e("1",""+e);
        }
        try{
            extraHeaders = Arrays.asList(new BasicNameValuePair("Cookie", "session=abcd"));
            client = new WebSocketClient(URI.create(Ayar.WebSoketAdres), new WebSocketClient.Listener() {
                @Override
                public void onConnect() {
                    Ayar.UygulamaServis=true;
                    Log.d("onConnect", "Bağlandı!");
                }
                @Override
                public void onMessage(String message) {
                    Log.d("onMessage", String.format("Gelen Mesaj: %s", message));
                    strGelenMesaj= Guvenlik.Hex2String(message);
                    Log.d("strGelenMesaj",strGelenMesaj);
                    //gelen mesaja göre işlem yap

                    try{
                        if(strGelenMesaj != null){
                            JSONObject j = new JSONObject(strGelenMesaj);
                            String kime =j.getString("b");
                            String s[] = kime.split("-");
                            int KimeFirma = Integer.parseInt(s[0]);
                            int KimeUye = Integer.parseInt(s[1]);

                            int SistemFirma=Integer.parseInt(Ayar.FirmaID);
                            int SistemUye=Integer.parseInt(Ayar.UyeID);

                            int durum = Integer.parseInt(j.getString("c"));
                            String detay = j.getString("d");

                            if(SistemFirma==KimeFirma && SistemUye==KimeUye){
                                // mesaj geldi
                                Log.d("Mesaj Bana","geldi...");

                                switch(durum){
                                    case 1:
                                        //yeni sipariş siparişleri yükle
                                        SiparisleriYukle();
                                        if(iSiparisToplam>0){

                                            Handler h1 = new Handler(Servis.this.getMainLooper());
                                            h1.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //ses çal kıpraş
                                                    Kipras();
                                                    if(Ayar.UygulamaAcik){
                                                        Toast.makeText(Servis.this,"Yeni Sipariş(ler) var",Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Bildirim("Yeni Sipariş","Henüz teslim edilmemiş "+iSiparisToplam+" Sipariş var.");
                                                    }

                                                    //ana sayfaya gönder
                                                }
                                            });
                                        }
                                        break;
                                    case 2:
                                        detay=Guvenlik.Hex2String(detay);
                                        //durum değişti
                                        if(detay !=null){
                                            JSONObject jj = new JSONObject(detay);
                                            final int sip=Integer.parseInt(jj.getString("sip"));
                                            final int dur=Integer.parseInt(jj.getString("durum"));
                                            db.GuncelleSiparis(sip,dur);
                                            Handler h = new Handler(Servis.this.getMainLooper());
                                            h.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //ses çal kıpraş
                                                    Kipras();

                                                    //String strMesaj=Fonksiyon.MesajBildirimDurum(sip, dur);
                                                    String strMesaj="Mesajbildirimdurum";
                                                    if(Ayar.UygulamaAcik){
                                                        Toast.makeText(Servis.this,strMesaj,Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Bildirim("Sipariş Durum Değişti",strMesaj);
                                                    }
                                                }
                                            });
                                            //ana sayfayı güncelle
                                            UzakDurumDegisti(sip,dur);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }catch(Exception e){
                        Log.e("Gelen Mesaj Json Hata",""+e);
                    }
                }

                @Override
                public void onMessage(byte[] data) {
                    //Log.d("onMessage byte", String.format("Got binary message! %s", toHexString(data));
                }

                @Override
                public void onDisconnect(int code, String reason) {
                    Log.d("onDisconnect", String.format("Koptu! Mesaj: %d Reason: %s", code, reason));
                }

                @Override
                public void onError(Exception error) {
                    Log.e("onError", "Error!", error);
                }
            }, extraHeaders);

            client.connect();

            //try{client.send("0");}catch(Exception e){}


        }catch(Exception e){
            Log.e(TAG, "Soket Başlat : "+e);
        }
    }

    //ses ve kıpraşım
    private void Kipras(){
        //ses çal
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bildirim);
        mediaPlayer.start();

        //vibrate

        // Get instance of Vibrator from current Context
        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Start immediately
        // Vibrate for 200 milliseconds
        // Sleep for 500 milliseconds
        long[] pattern = {0,200,300,400};


        // The "0" means to repeat the pattern starting at the beginning
        // CUIDADO: If you start at the wrong index (e.g., 1) then your pattern will be off --
        // You will vibrate for your pause times and pause for your vibrate times !
        vi.vibrate(pattern, -1);

        // Stop the Vibrator in the middle of whatever it is doing
        // CUIDADO: Do *not* do this immediately after calling .vibrate().
        // Otherwise, it may not have time to even begin vibrating!
        //vi.cancel();

    }

    //notification bildirim
    private void Bildirim(String Baslik,String Aciklama){
        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(Servis.this)
                .setSmallIcon(R.mipmap.ic_launcher) // small icon
                .setContentTitle(Baslik) //başlık
                .setContentText(Aciklama) //içerik
                .setAutoCancel(true); // clear notification after click

        Intent intent = new Intent(Servis.this, SayfaGiris.class);
        //PendingIntent pi = PendingIntent.getActivity(Servis.this,0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
        //mBuilder.setContentIntent(pi);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());
    }

    //siparişleri yükle
    private void SiparisleriYukle(){
        try{
            //json siparişleri yükle
            iSiparisToplam=0;
            JsonParser jsonParser = new JsonParser();
            List<NameValuePair> up = new ArrayList<NameValuePair>();
            up.add(new BasicNameValuePair("alp","Alptekin-Bodur"));
            up.add(new BasicNameValuePair("mobil",Ayar.UyeID)); // mobil kullanıcı id
            String json=jsonParser.Getir(Ayar.WebJsonSiparisListe,"POST",up);
            Log.d("Servis Json",json);
            jsonSiparisler=new JSONArray(json);
            if(jsonSiparisler != null){

                if(jsonSiparisler.length()>0){
                    //db temizle
                    db.SilSiparisTum();
                    Log.d("Servis","SilSiparisTum");


                    //siparişleri al
                    for (int i = 0; i < jsonSiparisler.length(); i++) {
                        iSiparisToplam+=1;
                        JSONObject j=jsonSiparisler.getJSONObject(i);
                        //json elementleri oku
                        int id=Integer.parseInt(j.getString("id"));
                        int tip=Integer.parseInt(j.getString("tip"));
                        int durum=Integer.parseInt(j.getString("durum"));
                        float toplam=Float.parseFloat(j.getString("toplam"));
                        float alat=Float.parseFloat(j.getString("lat"));
                        float alon=Float.parseFloat(j.getString("lon"));
                        String tarih=j.getString("tVerilis");
                        //String adres=Ayar.jsonTurkceKarekler(j.getString("adres"));
                        String tel=j.getString("tel");
                        //String isim=Fonksiyon.jsonTurkceKarekler(j.getString("isim"));
                        //String mesaj=Fonksiyon.jsonTurkceKarekler(j.getString("mesaj"));
                        //String ilce=Fonksiyon.jsonTurkceKarekler(j.getString("ilce"));
                        //String sehir=Fonksiyon.jsonTurkceKarekler(j.getString("sehir"));
                        String resim=j.getString("resim");
                        //db.EkleSiparis(id,tip,durum,toplam,alat,alon,tarih,adres,tel,...);

                        //String strGeceiMe ="id("+id+") tip("+tip+") durum("+durum+") toplam("+ toplam +") lat("+alat+") ";
                        //strGeceiMe+="lon("+alon+") tarih("+tarih+") adres("+adres+") tel("+tel+") isim("+isim+") ";
                        //strGeceiMe+="mesaj("+mesaj+") ilce("+ilce+") sehir("+sehir+") resim("+resim+") ";
                        //Log.d("Sipariş Ekle",strGeceiMe);


                        j=null;

                    }
                }else{
                    iSiparisToplam=0;
                    Log.e("jsonSiparisler","0 geldi");
                }
            }else{
                iSiparisToplam=0;
                Log.e("jsonSiparisler","null geldi");
            }
        }catch(Exception e){
            Log.e("Servis SiparisleriYukle",""+e);
        }
    }

    //gps başlat durdur
    private BroadcastReceiver mReceiverGpsBaslat = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int takip = intent.getIntExtra("takip",0);
            switch(takip){
                case 0:
                    gps.stopUsingGPS();
                    gps=null;
                    break;
                default:
                    gps=new Tracker(Servis.this);
                    if(!gps.canGetLocation()){
                        //gps yok
                    }
                    break;
            }
        }
    };

    //gps yanıt geldi
    private BroadcastReceiver mReceiverGps = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //String action = intent.getAction();
            int sapma = intent.getIntExtra("sapma",0);
            double lat = intent.getDoubleExtra("lat", 0);
            double lon = intent.getDoubleExtra("lon", 0);
            try{
                client.send(Islev.SoketMesajKoordinat(lat, lon, sapma));
            }catch(Exception e){
                Log.e("mReceiverGps",""+e);
            }
        }
    };

    //sipariş durum değişti
    private BroadcastReceiver mRecieverSip = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int sipNo =intent.getIntExtra("sipNo",0);
            int durum =intent.getIntExtra("durum",0);
            try{
                client.send(Islev.SoketMesajDurum(sipNo,durum));
            }catch(Exception e){
                Log.e("mRecieverSip",""+e);
            }
        }

    };

    //uzaktan sipariş durumu değişti ana sayfadaki siparişi güncelle
    private void UzakDurumDegisti(int sipNo,int durum) {
        Intent intent = new Intent("UzakDurumDegisti");
        sendLocationBroadcastUzak(intent,sipNo,durum);
    }
    private void sendLocationBroadcastUzak(Intent intent,int sipNo,int durum){
        intent.putExtra("sipNo",sipNo);
        intent.putExtra("durum",durum);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }



}
