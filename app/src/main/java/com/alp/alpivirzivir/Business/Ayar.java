package com.alp.alpivirzivir.Business;

public class Ayar {
    /*
     * public static String WebAdres = "http://10.0.2.2/CepRehberim";
     * public static String WebAdres = "http://192.168.1.155/CepRehberim";
     * public static String WebAdres = "http://www.ceprehberim.com";
     */
    public static String WebAdres = "http://www.ceprehberim.com";
    public static String WebSoketAdres = "ws://telsiz.siparisotomasyon.com:8080/alptekin";
    public static boolean WebSoketBaglami=false;

    public static String WebJsonSiparisListe = WebAdres + "/jsnsip/MobilSipListe/";
    public static String WebJsonSiparisDurumGuncelle = WebAdres + "/jsnsip/MobilSipDurum/";

    public static String WebJsonGiris = WebAdres + "/jsnsip/MobilGiris/";

    //Üye bilgileri
    public static boolean UyeGiris=false;
    public static String UyeID,UyeAdi,UyeResim,FirmaID,FirmaAdi,FirmaResim;
    public static boolean UygulamaAcik=false;
    public static boolean UygulamaServis=false;

    //koordinat ayarları
    public static double KoordinatLat=0;
    public static double KoordinatLon=0;

}
