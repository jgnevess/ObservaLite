package com.example.ObservaLite.utils;

public abstract class UrlBuilder {

    public static String UrlBuilder(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        if(!url.split("//")[0].equals("https:") && !url.split("//")[0].equals("http:")) {
            stringBuilder.append("https://").append(url);
            return  stringBuilder.toString().toLowerCase();
        }
        return stringBuilder.append(url).toString().toLowerCase();
    }

}
