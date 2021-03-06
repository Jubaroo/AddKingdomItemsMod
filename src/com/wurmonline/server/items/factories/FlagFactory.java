
package com.wurmonline.server.items.factories;

import com.wurmonline.server.items.KingdomFlag;
import org.requiem.mods.kingdomitems.Constants;
import org.requiem.mods.kingdomitems.Initiator;

import java.util.ArrayList;

/**
 * Creates every flag.
 */
public class FlagFactory {

    private static ArrayList<Integer> flagList = new ArrayList<>();

    public static void addAllFlags() {
        for (int i = 0; i < Constants.FLAG_LIST.length; i++) {
            int id= KingdomFlag.addFlag(Constants.FLAG_LIST[i],Constants.NAMES[i]);
            if (id!=0) {
                flagList.add(id);
            } else {
                Initiator.debug(Constants.NAMES[i] + " flag - cant' be created, id is 0");
            }
        }
    }
}
