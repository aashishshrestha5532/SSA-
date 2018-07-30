package com.alchemist.ssa.NetworkStuffs;

/**
 * Created by Ashish Alton on 7/8/2018.
 */

public class StringResource {

    private static String url;
    private static boolean isNetworkAvailable;


    public static void setUrl(String url) {
        StringResource.url = url;
    }

    public static String getUrl() {
        return url;
    }


    public static boolean isIsNetworkAvailable() {
        return isNetworkAvailable;
    }

    public static void setIsNetworkAvailable(boolean isNetworkAvailable) {
        StringResource.isNetworkAvailable = isNetworkAvailable;
    }
}



