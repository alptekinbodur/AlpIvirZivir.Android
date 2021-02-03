package com.alp.alpivirzivir.Data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.alp.alpivirzivir.Model.Siparis;
import java.util.ArrayList;
import java.util.List;

public class Veri extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "alptekin";
    private static final String TABLO_SIP="siparis";

    public Veri(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //sipariş tablosu
        db.execSQL("CREATE TABLE "+ TABLO_SIP +" (id INTEGER,tip INTEGER,durum INTEGER,toplam FLOAT,lat FLOAT,"
                + "lon FLOAT,tarih TEXT,adres TEXT,tel TEXT,isim TEXT,mesaj TEXT,ilce TEXT,sehir TEXT,resim TEXT);");
        ///db.execSQL("INSERT INTO "+ TABLO_SIP +" VALUES (1,0);");
        Log.d("TABLO OLUŞTUR","Sipariş tablosu oluşturuldu");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLO_SIP);
        onCreate(db);
    }

    /////////////// Siparişler ///////////////
    // Sipariş Listesi
    public List<Siparis> ListeSiparis(){
        try {
            List<Siparis> sip = new ArrayList<Siparis>();
            SQLiteDatabase db;
            db = this.getReadableDatabase();
            String strSql ="SELECT * FROM "+TABLO_SIP+" WHERE id<>0 ORDER BY id DESC;";
            Cursor c = db.rawQuery(strSql, null);
            if (c!=null){
                if (c.moveToFirst()){
                    do {
                        //db.execSQL("CREATE TABLE "+ TABLO_SIP +" (id INTEGER,tip INTEGER,durum INTEGER,toplam FLOAT,lat FLOAT,"
                        //		+ "lon FLOAT,tarih TEXT,adres TEXT,tel TEXT,isim TEXT,mesaj TEXT,ilce TEXT,sehir TEXT,resim TEXT);");
                        Siparis k = new Siparis();
                        k.sID(Integer.parseInt(c.getString(0)));
                        k.sTip(Integer.parseInt(c.getString(1)));
                        k.sAdres(c.getString(7));
                        k.sDurum(Integer.parseInt(c.getString(2)));
                        k.sToplam(Double.parseDouble(c.getString(3)));
                        k.sUyeAdi(c.getString(9));
                        k.sUyeResim(c.getString(13));
                        k.sIlce(c.getString(11));
                        k.sSehir(c.getString(12));
                        sip.add(k);
                    } while (c.moveToNext());
                }
            }
            strSql=null;c.close();db.close();
            return sip;
        } catch (Exception e){
            return null;
        }
    }

    // Sipariş Detay
    public String[] DetaySiparis(String gelenSipNo) {
        // TODO Auto-generated method stub
        try {
            String ar[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            Cursor c=db.query(TABLO_SIP, new String[] { "*" },
                    "id=?",
                    new String[] { String.valueOf(gelenSipNo) }, null, null, null, null);
            if(c!= null){
                if (c.moveToFirst()) {
                    ar=new String[c.getColumnCount()];
                    /***
                     *  0:ID
                     *  1:tip
                     *  2:durum
                     *  3:toplam
                     *  4:lat
                     *  5:lon
                     *  6:tarih
                     *  7:adres
                     *  8:tel
                     *  9:isim
                     *  10:mesaj
                     *  11:ilce
                     *  12:sehir
                     *  13:üye resim
                     */
                    ar[0]=c.getString(0);
                    ar[1]=c.getString(1);
                    ar[2]=c.getString(2);
                    ar[3]=c.getString(3);
                    ar[4]=c.getString(4);
                    ar[5]=c.getString(5);
                    ar[6]=c.getString(6);
                    ar[7]=c.getString(7);
                    ar[8]=c.getString(8);
                    ar[9]=c.getString(9);
                    ar[10]=c.getString(10);
                    ar[11]=c.getString(11);
                    ar[12]=c.getString(12);
                    ar[13]=c.getString(13);
                }
            }
            c.close();db.close();
            return ar;
        } catch (Exception e) {
            return null;
        }
    }

    // Sipariş Ekle
    //adres TEXT,tel TEXT,isim TEXT,mesaj TEXT,ilce TEXT,sehir TEXT);");
    public long EkleSiparis(int id,int tip,int durum,float toplam,float lat,float lon,String tarih,
                            String adres,String tel,String isim,String mesaj,String ilce,String sehir,String resim) {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data
            ContentValues v = new ContentValues();
            v.put("id", id);
            v.put("tip", tip);
            v.put("durum",durum);
            v.put("toplam",toplam);
            v.put("lat",lat);
            v.put("lon",lon);
            v.put("tarih",tarih);
            v.put("adres",adres);
            v.put("tel",tel);
            v.put("isim",isim);
            v.put("mesaj",mesaj);
            v.put("ilce",ilce);
            v.put("sehir",sehir);
            v.put("resim",resim); //üye resim
            long rows = db.insert(TABLO_SIP, null, v);
            db.close();
            return rows; // return rows inserted.
        } catch (Exception e) {
            return -1;
        }
    }

    // Sipariş Sil
    public long SilSiparis(int id){
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            long rows = db.delete(TABLO_SIP, "id=?",
                    new String[] {String.valueOf(id)});
            db.close();
            return rows; // return rows delete.
        } catch (Exception e) {
            return -1;
        }
    }

    // Sipariş Tümünü Sil
    public long SilSiparisTum(){
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            long rows = db.delete(TABLO_SIP, "id<>0",
                    new String[] { });
            db.close();
            return rows; // return rows delete.
        } catch (Exception e) {
            return -1;
        }
    }

    // Siparişi Güncelle
    public long GuncelleSiparis(int id,int durum){
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            ContentValues v = new ContentValues();
            v.put("durum",durum);
            long rows = db.update(TABLO_SIP,v," id = ?", new String[] {String.valueOf(id)});
            db.close();
            Log.d("GuncelleSipariş","güncellendi id("+id+") durum("+durum+")");
            return rows;
        } catch (Exception e) {
            Log.e("GuncelleSipariş",""+e);
            return -1;
        }
    }




}
