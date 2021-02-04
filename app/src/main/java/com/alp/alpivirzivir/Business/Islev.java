package com.alp.alpivirzivir.Business;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import android.location.Location;
import android.util.Log;

import static com.alp.alpivirzivir.Business.Guvenlik.String2Hex;

public class Islev {
    public static String paraYap(double iPara){
        NumberFormat n = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("A");
        dfs.setGroupingSeparator('.');
        dfs.setMonetaryDecimalSeparator(',');
        ((DecimalFormat) n).setDecimalFormatSymbols(dfs);
        return n.format(iPara);
    }
    public static String paraParcaTam(String strPara){
        int iVirgul = strPara.indexOf(",");
        return strPara.substring(0,iVirgul);
    }
    public static String paraParcaKurus(String strPara){
        int ivirgul = strPara.indexOf(",");
        return strPara.substring(ivirgul+1, ivirgul+3);
    }

    public static float KoordinatAralik(double aLat,double aLon,double bLat,double bLon){
        try{
            Location locA = new Location("A");
            locA.setLatitude(aLat);
            locA.setLongitude(aLon);
            Location locB = new Location("B");
            locB.setLatitude(bLat);
            locB.setLongitude(bLon);
            float aralik = locA.distanceTo(locB);
            locA=null;locB=null;
            return aralik;
        }catch(Exception e){
            return 0;
        }
    }

    //soket mesajlar
    public static String SoketMesajDurum(int SipNo,int Durum){
        //String str="{\"a\":\""+ FirmaID +"-"+ UyeID +"\",\"b\":\""+FirmaID+"-0\",\"c\":2,\"d\":{\"sip\":"+SipNo+",\"durum\":"+Durum+"}}";
        String str="";
        return String2Hex(str);
    }

    //soket koordinat gönder
    public static String SoketMesajKoordinat(double lat,double lon,int sapma){
        //String str="{\"a\":\""+ FirmaID +"-"+ UyeID +"\",\"b\":\""+FirmaID+"-0\",\"c\":4,\"d\":{\"lat\":"+lat+",\"lon\":"+lon+",\"sapma\":"+sapma+"}}";
        String str="";
        return String2Hex(str);
    }
}
