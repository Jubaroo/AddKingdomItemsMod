package com.wurmonline.server.items.factories;

import com.wurmonline.server.items.MagicCarpets;
import org.requiem.mods.kingdomitems.Constants;

import java.util.ArrayList;

import static org.requiem.mods.kingdomitems.Initiator.debug;

/**
 * Creates all variants of magic carpets
 */
public class MagicCarpetFactory {

    public static ArrayList<Integer> carpetList = new ArrayList<>();

    public static void addAllCarpets() {
        for (int i = 0; i < Constants.CARPET_LIST.length; i++) {
            int id= MagicCarpets.addCarpet(Constants.CARPET_LIST[i],Constants.CARPET_NAMES[i]);
            if (id!=0) {
                carpetList.add(id);
            } else {
                debug(Constants.CARPET_NAMES[i] + " carpet - cant' be created, id is 0");
            }
        }
    }
}

