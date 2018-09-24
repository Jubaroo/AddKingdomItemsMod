
package com.wurmonline.server.items;

import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.combat.ArmourTypes;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.requiem.mods.kingdomitems.Initiator;

import java.io.IOException;

/**
 * Add all pavilions.
 */
public class KingdomPavilion {

    public static int addPavilion(String model, String name) {
        Initiator.debug("Initiating Kingdom Pavilion " + model);
        try {
            return createItem(model, name);
        } catch (Exception e) {
            Initiator.debug("Initialization of pavilion failed: " + e.toString());
        }
        return 0;
    }

    private static int createItem(String model, String name) throws IOException {
        ItemTemplateBuilder builder = new ItemTemplateBuilder("org.kingdom.pavilion." + name);
        builder.name(name + " pavilion", name + " pavilions", "A pleasant open air tent designed for various kinds of "+name+" gatherings.");
        builder.descriptions("excellent", "good", "ok", "poor");
        builder.itemTypes(new short[]{24, 47, 109, 52, 86, 51, 98, 180, 182});
        builder.imageNumber((short) 640);
        builder.combatDamage(0);
        builder.decayTime(3024000L);
        builder.dimensions(5, 5, 100);
        builder.primarySkill(-10);
        builder.modelName(model + ".");
        builder.difficulty(10.0F);
        builder.weightGrams(2500);
        builder.material(Materials.MATERIAL_COTTON);
        builder.value(1);
        builder.isTraded(false);
        builder.behaviourType(BehaviourList.itemBehaviour);
        builder.containerSize(100, 200, 201);
        ItemTemplate result = builder.build();
        if (Initiator.pavilions) { createCreationEntry(result); }

        return result.getTemplateId();
    }

    private static void createCreationEntry(ItemTemplate newPavilion) {
        AdvancedCreationEntry pavilion = CreationEntryCreator.createAdvancedEntry(10044, 23, 213, newPavilion.getTemplateId(), false, false, 0.0f, true, true, CreationCategories.TENTS);
        pavilion.addRequirement(new CreationRequirement(1, 23, 10, true));
        pavilion.addRequirement(new CreationRequirement(2, 213, 6, true));
        pavilion.addRequirement(new CreationRequirement(3, 559, 10, true));
        pavilion.addRequirement(new CreationRequirement(4, 561, 10, true));
    }
}
