package com.wurmonline.server.items;

import com.wurmonline.server.items.factories.Constants;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.takino.mods.AddKingdomItems;

import java.io.IOException;

/**
 * Base for all wagons
 */
public class KingdomWagon {

    public static int addWagon(String model, String name) {
        AddKingdomItems.debug("Initing KingdomWagon " + model);
        try {
            return createItem(model, name);
        } catch (Exception e) {
            AddKingdomItems.debug("Initiatiation of wagon failed: " + e.toString());
        }
        return 0;
    }


    private static int createItem(String model, String name) throws IOException {
        AddKingdomItems.debug("id :  org.takino.wagon." + name);
        ItemTemplateBuilder builder = new ItemTemplateBuilder("org.takino.wagon." + name);
        builder.name(name + " wagon", name + " wagons", "A fairly large wagon designed to be dragged by four animals. " +
                "This design is used by "+ name + " kingdom.");
        builder.descriptions("almost full", "somewhat occupied", "half-full", "emptyish");
        builder.itemTypes(new short[]{(short)108, (short)1, (short)31,
                 (short)21, (short)51, (short)52, (short)44, (short)117,
                (short)193, (short)134, (short)47, (short)48, (short)176,
                 (short)180, (short)160, (short)54});
        builder.imageNumber((short)60);
        builder.combatDamage(0);
        builder.decayTime(9072000L);
        builder.dimensions(550,300,410);
        builder.primarySkill(-10);
        builder.modelName(model + ".");
        builder.size(3);

        builder.difficulty(70.0F);
        builder.weightGrams(240000);
        builder.material(Constants.BIRCHWOOD);
        builder.value('썐'); //?? same thing is set for wagon in CreationEntryCreator..
        builder.isTraded(false);
        builder.armourType(-1);
        builder.behaviourType((short) 41);

        ItemTemplate resultTemplate = builder.build();
        resultTemplate.setContainerSize(200, 260, 400);

        AddKingdomItems.debug(name + "; Template ID: " + resultTemplate.getTemplateId() + "; vehicle? " + resultTemplate.isVehicle());
        createCreationEntry(resultTemplate);

        return resultTemplate.getTemplateId();
    }


    private static void createCreationEntry(ItemTemplate newWwagon) {

        AdvancedCreationEntry wagon = CreationEntryCreator.createAdvancedEntry(10044, 22, 191, newWwagon.getTemplateId(),
                false, false, 0.0F, true, true, 0, 40.0D, CreationCategories.CARTS);

        wagon.addRequirement(new CreationRequirement(1, 191, 1, true));
        wagon.addRequirement(new CreationRequirement(2, 22, 20, true));
        wagon.addRequirement(new CreationRequirement(3, 23, 4, true));
        wagon.addRequirement(new CreationRequirement(4, 218, 10, true));
        wagon.addRequirement(new CreationRequirement(5, 632, 2, true));
        wagon.addRequirement(new CreationRequirement(6, 486, 2, true));
    }
}
