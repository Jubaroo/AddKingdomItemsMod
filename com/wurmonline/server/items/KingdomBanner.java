package com.wurmonline.server.items;

import com.wurmonline.server.items.factories.Constants;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.takino.mods.AddKingdomItems;

import java.io.IOException;

/**
 * Add all banners.
 */
public class KingdomBanner {

    public static int addBanner(String model, String name, boolean tall) {
        AddKingdomItems.debug("Initiating Kingdom Banner " + model);
        try {
            return createItem(model, name, tall);
        } catch (Exception e) {
            AddKingdomItems.debug("Initialization of banner failed: " + e.toString());
        }
        return 0;
    }

    private static int createItem(String model, String name, boolean tall) throws IOException {
        String itemId = "org.takino.banner.";
        if (tall) {
            itemId+="tall.";
        }
        ItemTemplateBuilder builder = new ItemTemplateBuilder(itemId + name);
        String add="";
        if (tall) {
            add=" tall";
        }
        builder.name(name + add + " banner", name + add + " banners", "An elegant symbol of allegiance and faith towards " + name);
        builder.descriptions("excellent", "good", "ok", "poor");
        builder.itemTypes(new short[]{(short) 24, (short) 92, (short) 147, (short) 51, (short) 52, (short) 109, (short) 48, (short) 86, (short) 119, (short) 44, (short) 199, (short) 173});
        builder.imageNumber((short) 640);
        builder.combatDamage(0);
        builder.decayTime(9072000L);
        builder.dimensions(5, 5, 205);
        builder.primarySkill(-10);
        builder.modelName(model + ".");
        builder.difficulty(40.0f);
        builder.weightGrams(2500);
        builder.material(Constants.BIRCHWOOD);
        builder.value(10000);
        builder.isTraded(true);
        builder.armourType(-1);
        builder.behaviourType((short)1);
        ItemTemplate result = builder.build();
        createCreationEntry(result);

        return result.getTemplateId();
    }

    private static void createCreationEntry(ItemTemplate newBanner) {
        CreationEntryCreator.createSimpleEntry(10016, 213, 23, newBanner.getTemplateId(),
        true, true, 0.0F, false, false, CreationCategories.FLAGS);
    }
}
