package com.wurmonline.server.items.factories;

import com.wurmonline.server.items.KingdomTower;
import org.requiem.mods.kingdomitems.AddKingdomItems;

import java.util.ArrayList;

/**
 * Creates all variants of towers
 */
public class TowerFactory {

    private static ArrayList<Integer> towerList = new ArrayList<>();

    public static void addAllTowers() {
        for (int i=0; i < Constants.TOWER_LIST.length; i++) {
            int id= KingdomTower.addTower(Constants.TOWER_LIST[i],Constants.NAMES[i]);
            if (id!=0) {
                towerList.add(id);
            } else {
                AddKingdomItems.debug(Constants.NAMES[i] + " tower - cant' be created, id is 0");
            }
        }
    }
}
