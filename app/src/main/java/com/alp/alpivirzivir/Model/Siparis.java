package com.alp.alpivirzivir.Model;

public class Siparis {
    int id, tip, durum;
    String uyeAdi,uyeResim, adres, ilce, sehir;
    double toplam;
    // Set Value
    public void sID(int vID){
        this.id = vID;
    }
    public void sTip(int vUst){
        this.tip = vUst;
    }
    public void sDurum(int vDurum){
        this.durum=vDurum;
    }
    public void sUyeAdi(String vUyeAdi){
        this.uyeAdi=vUyeAdi;
    }
    public void sUyeResim(String vUyeResim){
        this.uyeResim=vUyeResim;
    }
    public void sToplam(double vToplam){
        this.toplam=vToplam;
    }
    public void sAdres(String vAdres){
        this.adres=vAdres;
    }
    public void sIlce(String vIlce){
        this.ilce=vIlce;
    }
    public void sSehir(String vSehir){
        this.sehir=vSehir;
    }

    // Get Value
    public int ID(){
        return id;
    }
    public int Tip(){
        return tip;
    }
    public int Durum(){
        return durum;
    }
    public String UyeAdi(){
        return uyeAdi;
    }
    public String UyeResim(){
        return uyeResim;
    }
    public double Toplam(){
        return toplam;
    }
    public String Adres(){
        return adres;
    }
    public String Ilce(){
        return ilce;
    }
    public String Sehir(){
        return sehir;
    }
}
