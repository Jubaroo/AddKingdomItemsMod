
package com.wurmonline.server.items;

import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.combat.ArmourTypes;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.requiem.mods.kingdomitems.Initiator;

import java.io.IOException;

/**
 * Add all tents.
 */
public class KingdomMilitaryTent {

    public static int addTent(String model, String name) {
        Initiator.debug("Initiating Kingdom Tent " + model);
        try {
            return createItem(model, name);
        } catch (Exception e) {
            Initiator.debug("Initialization of tent failed: " + e.toString());
        }
        return 0;
    }

    private static int createItem(String model, String name) throws IOException {
        ItemTemplateBuilder builder = new ItemTemplateBuilder("org.kingdom.military.tent." + name);
        builder.name(name + " military tent", name + " military tents", "This is the standard tent for " + name + " military actions.");
        builder.descriptions("excellent", "good", "ok", "poor");
        builder.itemTypes(new short[]{24, 1, 47, 181, 109, 117, 52, 86, 98, 180});
        builder.imageNumber((short) 640);
        builder.combatDamage(0);
        builder.decayTime(3024000L);
        builder.dimensions(5, 5, 50);
        builder.primarySkill(-10);
        builder.modelName(model + ".");
        builder.difficulty(10.0F);
        builder.weightGrams(3500);
        builder.material(Materials.MATERIAL_COTTON);
        builder.value(1);
        builder.isTraded(false);
        builder.behaviourType(BehaviourList.itemBehaviour);
        builder.containerSize(100, 200, 201);
        ItemTemplate result = builder.build();
        createCreationEntry(result);

        return result.getTemplateId();
    }

    private static void createCreationEntry(ItemTemplate newTent) {
        if (Initiator.tents) {
            AdvancedCreationEntry tentMilitary = CreationEntryCreator.createAdvancedEntry(10044, 23, 213, newTent.getTemplateId(), false, false, 0.0f, true, true, CreationCategories.TENTS);
            tentMilitary.addRequirement(new CreationRequirement(1, 23, 10, true));
            tentMilitary.addRequirement(new CreationRequirement(2, 213, 12, true));
            tentMilitary.addRequirement(new CreationRequirement(3, 559, 10, true));
            tentMilitary.addRequirement(new CreationRequirement(4, 561, 10, true));
        }
    }
}