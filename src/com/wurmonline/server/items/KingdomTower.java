package com.wurmonline.server.items;

import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.combat.ArmourTypes;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.requiem.mods.kingdomitems.AddKingdomItems;

import java.io.IOException;

/**
 * For creation of towers
 */
public class KingdomTower {

    public static int addTower(String model, String name) {
        AddKingdomItems.debug("Initiating Kingdom Tower " + model);
        try {
            return createItem(model, name);
        } catch (Exception e) {
            AddKingdomItems.debug("Initialization of tower failed: " + e.toString());
        }
        return 0;
    }

    private static int createItem(String model, String name) throws IOException {
        ItemTemplateBuilder builder = new ItemTemplateBuilder("org.kingdom.tower." + name);
        builder.name(name + " tower", name + " tower", "A high guard tower.");
        builder.descriptions("excellent", "good", "ok", "poor");
        builder.itemTypes(new short[]{52, 25, 31, 67, 44, 85, 86, 49, 98, 123, 194, 239});
        builder.imageNumber((short) 60);
        builder.combatDamage(0);
        builder.decayTime(19353600L);
        builder.dimensions(400, 400, 600);
        builder.primarySkill(-10);
        builder.modelName(model + ".");
        builder.difficulty(20.0F);
        builder.weightGrams(500000);
        builder.material(Materials.MATERIAL_STONE);
        builder.armourType(ArmourTypes.ARMOUR_NONE);
        builder.behaviourType(BehaviourList.itemBehaviour);
        ItemTemplate result = builder.build();
        createCreationEntry(result);

        return result.getTemplateId();
    }

    private static void createCreationEntry(ItemTemplate newTower) {
        AdvancedCreationEntry towerStone = CreationEntryCreator.createAdvancedEntry(1013, 132, 130, newTower.getTemplateId(), false, false, 0.0f, true, true, CreationCategories.TOWERS);
        towerStone.addRequirement(new CreationRequirement(1, 132, 500, true));
        towerStone.addRequirement(new CreationRequirement(2, 130, 500, true));
        towerStone.addRequirement(new CreationRequirement(3, 22, 100, true));
    }
}
