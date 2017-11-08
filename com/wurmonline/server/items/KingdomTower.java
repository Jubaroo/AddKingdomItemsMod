package com.wurmonline.server.items;

import com.wurmonline.server.items.factories.Constants;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.takino.mods.AddKingdomItems;

import java.io.IOException;

/**
 * For creation of towers
 */
public class KingdomTower {

    public static int addTower(String model, String name) {
        AddKingdomItems.debug("Initing KingdomTower " + model);
        try {
            return createItem(model, name);
        } catch (Exception e) {
            AddKingdomItems.debug("Initiatiation of tower failed: " + e.toString());
        }
        return 0;
    }

    private static int createItem(String model, String name) throws IOException {
        ItemTemplateBuilder builder = new ItemTemplateBuilder("org.takino.tower." + name);
        builder.name(name + " tower", name + " towers", "A high guard tower.");
        builder.descriptions("excellent", "good", "ok", "poor");
        builder.itemTypes(new short[]{(short) 52, (short) 25, (short) 31,
                (short) 67, (short) 44, (short) 85, (short) 86, (short) 49,
                (short) 98, (short) 123, (short) 194, (short) 239});
        builder.imageNumber((short) 60);
        builder.combatDamage(0);
        builder.decayTime(19353600L);
        builder.dimensions(400, 400, 600);
        builder.primarySkill(-10);
        builder.modelName(model + ".");
        builder.difficulty(20.0F);
        builder.weightGrams(500000);
        builder.material(Constants.STONE);
        builder.armourType(-1);
        builder.behaviourType((short)1);
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
