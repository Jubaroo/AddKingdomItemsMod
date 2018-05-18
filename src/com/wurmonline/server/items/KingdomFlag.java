package com.wurmonline.server.items;

import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.combat.ArmourTypes;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.requiem.mods.kingdomitems.AddKingdomItems;

import java.io.IOException;

/**
 * For creation of flags
 */
public class KingdomFlag {

    public static int addFlag(String model, String name) {
        AddKingdomItems.debug("Initiating Kingdom Flag " + model);
        try {
            return createItem(model, name);
        } catch (Exception e) {
            AddKingdomItems.debug("Initialization of wagon failed: " + e.toString());
        }
        return 0;
    }

    private static int createItem(String model, String name) throws IOException {
        ItemTemplateBuilder builder = new ItemTemplateBuilder("org.takino.flag." + name);
        builder.name(name + " flag", name + " flags", "A symbol of " + name + " kingdom.");
        builder.descriptions("excellent", "good", "ok", "poor");
        builder.itemTypes(new short[]{24, 92, 124, 147, 52, 109, 48, 86, 119, 44, 199, 173});
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
        builder.armourType(ArmourTypes.ARMOUR_NONE);
        builder.behaviourType(BehaviourList.itemBehaviour);
        ItemTemplate result = builder.build();
        createCreationEntry(result);

        return result.getTemplateId();
    }
    private static void createCreationEntry(ItemTemplate newBanner) {
        CreationEntryCreator.createSimpleEntry(10016, 213, 23, newBanner.getTemplateId(),
        true, true, 0.0F, false, false, CreationCategories.FLAGS);
    }
}
