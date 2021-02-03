package com.alp.alpivirzivir.Business;

public class Dogrumu {

    public static boolean isEmail(String email) {
        try{
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }catch(Exception e){
            return false;
        }
    }
    public static boolean isTel(String strTel){
        try{
            strTel = strTel.replace("(", "");
            strTel = strTel.replace(")", "");
            strTel = strTel.replace("+", "");
            strTel = strTel.replace(" ", "");
            String regexStr = "^[0-9]$";
            if (strTel.length() == 10){
                return true;
            } else {
                return false;
            }
        }catch(Exception e){
            return false;
        }
    }
}
