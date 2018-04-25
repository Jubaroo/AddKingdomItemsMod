package com.wurmonline.server.items;

import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.combat.ArmourTypes;
import com.wurmonline.server.items.factories.Constants;
import com.wurmonline.server.skills.SkillList;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.requiem.mods.kingdomitems.AddKingdomItems;

import java.io.IOException;

/**
 * Base for all wagons
 */
public class KingdomWagons implements ItemTypes {

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
        builder.itemTypes(new short[]{
                ITEM_TYPE_NAMED,
                ITEM_TYPE_HOLLOW,
                ITEM_TYPE_NOTAKE,
                ITEM_TYPE_WOOD,
                ITEM_TYPE_TURNABLE,
                ITEM_TYPE_DECORATION,
                ITEM_TYPE_REPAIRABLE,
                ITEM_TYPE_VEHICLE,
                ITEM_TYPE_CART,
                ITEM_TYPE_VEHICLE_DRAGGED,
                ITEM_TYPE_LOCKABLE,
                ITEM_TYPE_HASDATA,
                ITEM_TYPE_TRANSPORTABLE,
                ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                ITEM_TYPE_NOWORKPARENT,
                ITEM_TYPE_NORENAME
        });
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
        builder.armourType(ArmourTypes.ARMOUR_NONE);
        builder.behaviourType(BehaviourList.vehicleBehaviour);
        builder.dyeAmountOverrideGrams((short) 0);
        builder.containerSize(200,260,400);
        ItemTemplate resultTemplate = builder.build();
        AddKingdomItems.debug(name + "; Template ID: " + resultTemplate.getTemplateId() + "; vehicle? " + resultTemplate.isVehicle());
        createCreationEntry(resultTemplate);

        return resultTemplate.getTemplateId();
    }

    private static void createCreationEntry(ItemTemplate newWwagon) {

        AdvancedCreationEntry wagon = CreationEntryCreator.createAdvancedEntry(
                SkillList.CARPENTRY_FINE, ItemList.plank, ItemList.wheelAxleSmall, newWwagon.getTemplateId(),
        false, false, 0.0F, true, true, 0, AddKingdomItems.minSkill, CreationCategories.CARTS);// min skill 40.0D
        wagon.addRequirement(new CreationRequirement(1, ItemList.wheelAxleSmall, 1, true));
        wagon.addRequirement(new CreationRequirement(2, ItemList.plank, 20, true));
        wagon.addRequirement(new CreationRequirement(3, ItemList.shaft, 4, true));
        wagon.addRequirement(new CreationRequirement(4, ItemList.nailsIronSmall, 10, true));
        wagon.addRequirement(new CreationRequirement(5, ItemList.yoke, 2, true));
        wagon.addRequirement(new CreationRequirement(6, ItemList.sheet, 2, true));
    }
}
