package com.wurmonline.server.items.factories;

import com.wurmonline.server.items.KingdomBanner;
import org.takino.mods.AddKingdomItems;

import java.util.ArrayList;

/**
 * Creates all kinds of Banners.
 */
public class BannerFactory {
    public static ArrayList<Integer> bannerList = new ArrayList<>();
    public static void addAllBanners() {
        for (int i=0; i < Constants.BANNER_LIST.length; i++) {
            int id = KingdomBanner.addBanner(Constants.BANNER_LIST[i],Constants.NAMES[i],false);
            if (id!=0) {
                bannerList.add(id);
            } else {
                AddKingdomItems.debug(Constants.NAMES[i] + " banner - cant' be created, id is 0");
            }
            if (!Constants.BANNER_TALL_LIST[i].equals("")) {
                id = KingdomBanner.addBanner(Constants.BANNER_TALL_LIST[i], Constants.NAMES[i], true);
                if (id != 0) {
                    bannerList.add(id);
                } else {
                    AddKingdomItems.debug(Constants.NAMES[i] + " banner - cant' be created, id is 0");
                }
            }
        }
    }
}
