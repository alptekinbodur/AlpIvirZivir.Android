package com.alp.alpivirzivir.Model;
public class Login {
    int Id;
    String Isim,Resim;

    //Set value
    public void  SetID(int id){
        Id=id;
    }

    public void setIsim(String isim) {
        Isim = isim;
    }

    public void setResim(String resim) {
        Resim = resim;
    }

    //get value

    public int getId() {
        return Id;
    }

    public String getIsim() {
        return Isim;
    }

    public String getResim() {
        return Resim;
    }
}
