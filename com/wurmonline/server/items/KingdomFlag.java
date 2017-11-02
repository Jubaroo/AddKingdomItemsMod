package com.wurmonline.server.items;


import com.wurmonline.server.items.factories.Constants;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.takino.mods.AddKingdomItems;

import java.io.IOException;

/**
 * For creation of flags
 */
public class KingdomFlag {

    public static int addFlag(String model, String name) {
        //createItemTemplate(579, "kingdom flag", "flags", "excellent", "good", "ok", "poor",
        // "Apart from being a symbol of your allegiance and territorial demands,
        // this is also a good indication of where the wind is blowing.",
        // new short[]{(short)24, (short)92, (short)124, (short)52, (short)109, (short)48, (short)86,
        // (short)119, (short)44, (short)173, (short)199}, (short)640, (short)1, 0, 9072000L,
        // 5, 5, 205, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.decoration.flag.",
        // 40.0F, 2500, (byte)14, 10000, true);
        try {
            return createItem(model, name);
        } catch (Exception e) {
            AddKingdomItems.debug("Initiatiation of wagon failed: " + e.toString());
        }
        return 0;

    }

    public static int createItem(String model, String name) throws IOException {
        ItemTemplateBuilder builder = new ItemTemplateBuilder("org.takino.flag." + name);
        builder.name(name + " flag", name + " flags", "A symbol of " + name + " kingdom.");
        builder.descriptions("excellent", "good", "ok", "poor");
        builder.itemTypes(new short[]{(short) 24, (short) 92, (short) 147,
                (short) 51, (short) 52, (short) 109, (short) 48, (short) 86,
                (short) 119, (short) 44, (short) 199, (short) 173});
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
