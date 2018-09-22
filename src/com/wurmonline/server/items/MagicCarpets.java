
package com.wurmonline.server.items;

import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.combat.ArmourTypes;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.ItemMaterials;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.requiem.mods.kingdomitems.Initiator;

import java.io.IOException;

import static org.requiem.mods.kingdomitems.Initiator.debug;

public class MagicCarpets implements ItemTypes {

    public static int addCarpet(String model, String name) {
        debug("Initiating Requiem Magic Carpets " + model);
        try {
            return createItem(model, name);
        } catch (Exception e) {
            debug("Initialization of magic carpets failed: " + e.toString());
        }
        return 0;
    }

    private static int createItem(String model, String name) throws IOException {
        debug("id :  requiem.magic.carpet." + name);
        ItemTemplateBuilder builder = new ItemTemplateBuilder("requiem.magic.carpet." + name);
        builder.size(3);
        builder.name(name + " magic carpet", name + " magic carpets", "A hovering magical carpet able to carry a fully grown person anywhere they wish. You can pick it up and take it with you wherever you need to go. This version cannot hold any cargo or go in water however.");
        builder.descriptions("Excellent", "good", "ok", "poor");
        builder.itemTypes(new short[]{
                ITEM_TYPE_NAMED,
                ITEM_TYPE_CLOTH,
                ITEM_TYPE_COLORABLE,
                ITEM_TYPE_TURNABLE,
                ITEM_TYPE_DECORATION,
                ITEM_TYPE_REPAIRABLE,
                ITEM_TYPE_FLOATING,
                ITEM_TYPE_VEHICLE,
                ITEM_TYPE_HASDATA,
                ITEM_TYPE_NOT_MISSION,
                ITEM_TYPE_NOWORKPARENT,
                ITEM_TYPE_NORENAME
        });

        builder.imageNumber((short)901);
        builder.behaviourType(BehaviourList.vehicleBehaviour);
        builder.combatDamage(0);
        builder.decayTime(9072000L);
        builder.dimensions(200,150,5);
        builder.primarySkill(-10);
        builder.modelName(model + ".");
        builder.difficulty(Initiator.carpetDifficulty);
        builder.weightGrams(Initiator.carpetWeightGrams);
        builder.material(ItemMaterials.MATERIAL_COTTON);
        builder.value(75000);
        builder.isTraded(true);
        builder.dyeAmountOverrideGrams((short) 0);
        ItemTemplate resultTemplate = builder.build();
        debug(name + "; Template ID: " + resultTemplate.getTemplateId() + "; carpet? " + resultTemplate.isVehicle());
        if (Initiator.magicCarpets) { createCreationEntry(resultTemplate); }

        return resultTemplate.getTemplateId();
    }

    private static void createCreationEntry(ItemTemplate newCarpet) {

        AdvancedCreationEntry carpet = CreationEntryCreator.createAdvancedEntry(SkillList.CLOTHTAILORING, ItemList.loom, ItemList.clothString, newCarpet.getTemplateId(), false, true, 0.0F, false, false, 0, Initiator.carpetMinSkill, CreationCategories.CARTS);
        carpet.addRequirement(new CreationRequirement(1, ItemList.clothString, 19, true));
        carpet.addRequirement(new CreationRequirement(2, ItemList.sheet, 3, true));
        carpet.addRequirement(new CreationRequirement(3, ItemList.leatherStrip, 4, true));
        carpet.addRequirement(new CreationRequirement(4, ItemList.sourceSalt, 10, true));
    }
}
