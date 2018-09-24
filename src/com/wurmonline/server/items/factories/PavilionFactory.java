
package com.wurmonline.server.items.factories;

import com.wurmonline.server.items.KingdomPavilion;
import org.requiem.mods.kingdomitems.Constants;
import org.requiem.mods.kingdomitems.Initiator;

import java.util.ArrayList;

/**
 * Creates all kinds of pavilions.
 */
public class PavilionFactory {

    private static ArrayList<Integer> pavilionList = new ArrayList<>();

    public static void addAllPavilions() {
        for (int i = 0; i < Constants.PAVILION_LIST.length; i++) {
            int id= KingdomPavilion.addPavilion(Constants.PAVILION_LIST[i],Constants.NAMES[i]);
            if (id!=0) {
                pavilionList.add(id);
            } else {
                Initiator.debug(Constants.NAMES[i] + " pavilion - cant' be created, id is 0");
            }
        }
    }
}
