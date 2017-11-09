package com.wurmonline.server.items.factories;

import com.wurmonline.server.items.KingdomTent;
import org.takino.mods.AddKingdomItems;

import java.util.ArrayList;

/**
 * Creates all kinds of tents.
 */
public class TentFactory {
    private static ArrayList<Integer> tentList = new ArrayList<>();

    public static void addAllTents() {
        for (int i=0; i < Constants.TENT_LIST.length; i++) {
            int id= KingdomTent.addTent(Constants.TENT_LIST[i],Constants.NAMES[i]);
            if (id!=0) {
                tentList.add(id);
            } else {
                AddKingdomItems.debug(Constants.NAMES[i] + " tent - cant' be created, id is 0");
            }
        }
    }
}
