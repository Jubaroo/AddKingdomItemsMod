
package com.wurmonline.server.items.factories;

import com.wurmonline.server.items.KingdomMilitaryTent;
import org.requiem.mods.kingdomitems.Constants;
import org.requiem.mods.kingdomitems.Initiator;

import java.util.ArrayList;

/**
 * Creates all kinds of tents.
 */
public class MilitaryTentFactory {

    private static ArrayList<Integer> tentList = new ArrayList<>();

    public static void addAllTents() {
        for (int i = 0; i < Constants.TENT_LIST.length; i++) {
            int id= KingdomMilitaryTent.addTent(Constants.TENT_LIST[i],Constants.MILITARY_TENT_NAMES[i]);
            if (id!=0) {
                tentList.add(id);
            } else {
                Initiator.debug(Constants.MILITARY_TENT_NAMES[i] + " tent - cant' be created, id is 0");
            }
        }
    }
}
