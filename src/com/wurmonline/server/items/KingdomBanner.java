
package com.wurmonline.server.items;

import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.combat.ArmourTemplate;
import com.wurmonline.server.combat.ArmourTypes;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.requiem.mods.kingdomitems.Initiator;

import java.io.IOException;

/**
 * Add all banners.
 */
public class KingdomBanner {

    public static int addBanner(String model, String name, boolean tall) {
        Initiator.debug("Initiating Kingdom Banner " + model);
        try {
            return createItem(model, name, tall);
        } catch (Exception e) {
            Initiator.debug("Initialization of banner failed: " + e.toString());
        }
        return 0;
    }

    private static int createItem(String model, String name, boolean tall) throws IOException {
        String itemId = "org.kingdom.banner.";
        if (tall) {
            itemId += "tall.";
        }
        ItemTemplateBuilder builder = new ItemTemplateBuilder(itemId + name);
        String add = "";
        if (tall) {
            add = " tall";
        }
        builder.name(name + add + " banner", name + add + " banners", "An elegant symbol of allegiance and faith towards " + name);
        builder.descriptions("excellent", "good", "ok", "poor");
        builder.itemTypes(new short[]{24, 92, 147, 51, 52, 109, 48, 86, 119, 44, 199, 173});
        builder.imageNumber((short) 640);
        builder.combatDamage(0);
        builder.decayTime(9072000L);
        builder.dimensions(5, 5, 205);
        builder.primarySkill(-10);
        builder.modelName(model + ".");
        builder.difficulty(40.0f);
        builder.weightGrams(2500);
        builder.material(Materials.MATERIAL_WOOD_BIRCH);
        builder.value(10000);
        builder.isTraded(true);
        builder.behaviourType(BehaviourList.itemBehaviour);
        ItemTemplate result = builder.build();
        createCreationEntry(result);
        return result.getTemplateId();
    }

    private static void createCreationEntry(ItemTemplate newBanner) {
        if (Initiator.banners) {
            CreationEntryCreator.createSimpleEntry(10016, 213, 23, newBanner.getTemplateId(), true, true, 0.0F, false, false, CreationCategories.FLAGS);
        }
    }
}