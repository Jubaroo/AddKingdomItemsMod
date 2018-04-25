package com.wurmonline.server.items.factories;

import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.behaviours.Vehicle;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.MagicCarpets;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.requiem.mods.kingdomitems.util.VehicleFacadeImpl;

import java.util.ArrayList;
import java.util.List;

import static org.requiem.mods.kingdomitems.AddKingdomItems.debug;

public class MagicCarpetFactory {

    static ArrayList<Integer> carpetList = new ArrayList<>();

    public static void addAllCarpets() {
        for (int i = 0; i < Constants.CARPET_LIST.length; i++) {
            int id= MagicCarpets.addCarpet(Constants.CARPET_LIST[i],Constants.CARPET_NAMES[i]);
            if (id!=0) {
                carpetList.add(id);
            } else {
                debug(Constants.CARPET_NAMES[i] + " carpet - cant' be created, id is 0");
            }
        }
    }

    public static void createCreationEntries() {
        for (int id: carpetList) {
            MagicCarpets.createCreationEntry(id);
        }
    }

    public static void registerCarpetManageHook() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.Creature"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item")
            };
            CtClass output = HookManager.getInstance().getClassPool().get("java.util.List");
            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.VehicleBehaviour", "getVehicleBehaviours",
                    Descriptor.ofMethod(output, input), () -> (proxy, method, args) -> {
                        List<ActionEntry> original = (List<ActionEntry>) method.invoke(proxy, args);
                        Item item = (Item) args[1];
                        Creature performer = (Creature) args[0];
                        if (item.mayManage(performer)) {
                            int itemId = item.getTemplateId();
                            for (int id : MagicCarpetFactory.carpetList) {
                                if (id == itemId) {
                                    debug("Adding manage permissions");
                                    original.add(Actions.actionEntrys[Actions.MANAGE_VEHICLE]);
                                }
                            }
                        }
                        if (item.maySeeHistory(performer)) {
                            int itemId = item.getTemplateId();
                            for (int id : MagicCarpetFactory.carpetList) {
                                if (id == itemId) {
                                    original.add(new ActionEntry(Actions.SHOW_HISTORY_FOR_OBJECT, "History of Carpet", "viewing"));
                                }
                            }
                        }
                        return original;
                    });
        }
        catch (Exception e) {
            debug("Permission hook for magic carpet: " + e.toString());
        }
    }

    public static void registerCarpetHook() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.Vehicle")
            };
            CtClass output = CtPrimitiveType.voidType;
            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.Vehicles", "setSettingsForVehicle",
                    Descriptor.ofMethod(output, input), () -> (proxy, method, args) -> {
                        debug("Adding vehicle configuration for magic carpets");
                        Item item = (Item) args[0];
                        int templateId = item.getTemplateId();
                        for (int i: MagicCarpetFactory.carpetList) {
                            if (i==templateId) {
                                Vehicle vehicle = (Vehicle) args[1];
                                VehicleFacadeImpl vehfacade = new VehicleFacadeImpl(vehicle);
                                vehfacade.createPassengerSeats(0);
                                vehfacade.setPilotName("rider");
                                vehfacade.setCreature(false);
                                vehfacade.setEmbarkString("ride");
                                vehfacade.setEmbarksString("rides");

                                vehicle.name = item.getName();
                                vehicle.setSeatFightMod(0, 0.9f, 0.3f);
                                vehicle.setSeatOffset(0, 0.0f, 0.0f, 0.0f, 1.453f);
                                vehicle.maxHeightDiff = 0.04f;
                                vehicle.maxDepth = -0.7f;
                                vehicle.skillNeeded = 23.0f;
                                vehfacade.setMaxSpeed(2.0f);
                                vehicle.commandType = 2;
                                vehicle.setMaxAllowedLoadDistance(4);
                                return null;
                            }
                        }
                        return method.invoke(proxy,args);
                    });
        } catch (NotFoundException e) {
            debug("Magic Carpet hook: " + e.toString());
        }
    }

}

