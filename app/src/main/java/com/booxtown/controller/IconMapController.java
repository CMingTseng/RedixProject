package com.booxtown.controller;

/**
 * Created by thuyetpham94 on 14/09/2016.
 */
public class IconMapController {
    public static String icon(String swap,String free,String buy){
        if(swap.contains("1")&free.contains("0")&buy.contains("0")){
            return "icon_swap";
        }
        if(swap.contains("0")&free.contains("1")&buy.contains("0")){
            return "icon_buy";
        }
        if(swap.contains("0")&free.contains("0")&buy.contains("1")){
            return "icon_free";
        }
        if(swap.contains("1")&free.contains("1")&buy.contains("0")){
            return "icon_2_option";
        }
        if(swap.contains("1")&free.contains("1")&buy.contains("1")){
            return "icon_3_option";
        }
        if(swap.contains("1")&free.contains("0")&buy.contains("1")){
            return "icon_2_option";
        }
        if(swap.contains("0")&free.contains("1")&buy.contains("1")){
            return "icon_2_option";
        }else {
            return "location_default";
        }
    }

    public static String iconExplorer(String swap,String free,String buy){
        if(swap.contains("1")&free.contains("0")&buy.contains("0")){
            return "icon_swap";
        }
        if(swap.contains("0")&free.contains("1")&buy.contains("0")){
            return "icon_buy";
        }
        if(swap.contains("0")&free.contains("0")&buy.contains("1")){
            return "icon_free";
        }
        if(swap.contains("1")&free.contains("1")&buy.contains("0")){
            return "swapbuy";
        }
        if(swap.contains("1")&free.contains("1")&buy.contains("1")){
            return "option";
        }
        if(swap.contains("1")&free.contains("0")&buy.contains("1")){
            return "swapfree";
        }
        if(swap.contains("0")&free.contains("1")&buy.contains("1")){
            return "freebuy";
        }else {
            return null;
        }
    }

}
