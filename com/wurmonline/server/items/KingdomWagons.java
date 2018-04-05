package com.wurmonline.server.items;

import com.wurmonline.server.items.factories.Constants;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.requiem.mods.kingdomitems.AddKingdomItems;

import java.io.IOException;

/**
 * Base for all wagons
 */
public class KingdomWagons {

    public static int addWagon(String model, String name) {
        AddKingdomItems.debug("Initiating Kingdom Wagon " + model);
        try {
            return createItem(model, name);
        } catch (Exception e) {
            AddKingdomItems.debug("Initialization of wagon failed: " + e.toString());
        }
        return 0;
    }

    private static int createItem(String model, String name) throws IOException {
        AddKingdomItems.debug("id :  org.kingdom.wagon." + name);
        ItemTemplateBuilder builder = new ItemTemplateBuilder("org.kingdom.wagon." + name);
        builder.name(name + " wagon", name + " wagons", "A fairly large wagon designed to be dragged by four animals. " + "This design is used by "+ name + " kingdom.");
        builder.descriptions("almost full", "somewhat occupied", "half-full", "emptyish");
        builder.itemTypes(new short[]{108, 1, 31, 21, 51, 52, 44, 117, 193, 134, 47, 48, 176, 180, 160, 54});
        builder.imageNumber((short)60);
        builder.combatDamage(0);
        builder.decayTime(9072000L);
        builder.dimensions(550,300,410);
        builder.primarySkill(-10);
        builder.modelName(model + ".");
        builder.size(3);
        builder.difficulty(AddKingdomItems.difficulty);//70.0F
        builder.weightGrams(AddKingdomItems.weightGrams);//240000
        builder.material(Constants.BIRCHWOOD);
        builder.value(50000);
        builder.isTraded(false);
        builder.armourType(-1);
        builder.behaviourType((short) 41);
        builder.dyeAmountOverrideGrams((short) 0);
        builder.containerSize(200,260,400);
        ItemTemplate resultTemplate = builder.build();
        AddKingdomItems.debug(name + "; Template ID: " + resultTemplate.getTemplateId() + "; vehicle? " + resultTemplate.isVehicle());
        createCreationEntry(resultTemplate);

        return resultTemplate.getTemplateId();
    }

    private static void createCreationEntry(ItemTemplate newWwagon) {

        AdvancedCreationEntry wagon = CreationEntryCreator.createAdvancedEntry(10044, 22, 191, newWwagon.getTemplateId(),
        false, false, 0.0F, true, true, 0, AddKingdomItems.minSkill, CreationCategories.CARTS);// min skill 40.0D
        wagon.addRequirement(new CreationRequirement(1, 191, 1, true));
        wagon.addRequirement(new CreationRequirement(2, 22, 20, true));
        wagon.addRequirement(new CreationRequirement(3, 23, 4, true));
        wagon.addRequirement(new CreationRequirement(4, 218, 10, true));
        wagon.addRequirement(new CreationRequirement(5, 632, 2, true));
        wagon.addRequirement(new CreationRequirement(6, 486, 2, true));
    }
}
