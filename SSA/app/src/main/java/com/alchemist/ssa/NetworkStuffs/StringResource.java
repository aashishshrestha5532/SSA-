package com.alchemist.ssa.NetworkStuffs;

/**
 * Created by Ashish Alton on 7/8/2018.
 */

public class StringResource {

    private static String url,username,email,present,total,total_marks;
    private static boolean isNetworkAvailable;
    private static int totalsubject;


    public static void setUrl(String url) {
        StringResource.url = url;
    }

    public static String getUrl() {
        return url;
    }
    public static void setPresent(String present){
        StringResource.present=present;
    }

    public static String getPresent() {
        return present;
    }
    public static void setTotal(String total){
        StringResource.total=total;
    }

    public static String getTotal() {
        return total;
    }


    public static void setTotal_marks(String total_marks){
        StringResource.total_marks=total_marks;
    }

    public static String getTotal_marks() {
        return total_marks;
    }

    public static boolean isIsNetworkAvailable() {
        return isNetworkAvailable;
    }

    public static void setIsNetworkAvailable(boolean isNetworkAvailable) {
        StringResource.isNetworkAvailable = isNetworkAvailable;
    }
    public static void setUsername(String username){
        StringResource.username=username;
    }

    public static String getUsername() {
        return username;
    }

    public static void setEmail(String email) {
        StringResource.email = email;
    }

    public static String getEmail() {
        return email;
    }
}



