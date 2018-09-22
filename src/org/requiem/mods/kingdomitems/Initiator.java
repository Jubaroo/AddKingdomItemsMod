package org.requiem.mods.kingdomitems;

import com.wurmonline.server.Features;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.behaviours.Seat;
import com.wurmonline.server.behaviours.Vehicle;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.factories.*;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.ItemTemplatesCreatedListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.requiem.mods.kingdomitems.util.SeatsFacadeImpl;
import org.requiem.mods.kingdomitems.util.VehicleFacadeImpl;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
/**
 * Adds various kingdom items and a magic carpet.
 */
public class Initiator implements WurmServerMod, Configurable, ItemTemplatesCreatedListener {

    private static final Logger logger;
    public static boolean   wagons;
    public static boolean   magicCarpets;
    public static boolean   towers;
    public static boolean   flags;
    public static boolean   tents;
    public static boolean   pavilions;
    public static boolean   banners;

    private static float    wagonMaxSpeed;
    private static float    wagonMaxDepth;
    private static float    wagonMaxHeightDiff;
    public static float     wagonDifficulty;
    private static int      wagonMaxAllowedLoadDistance;
    private static float    wagonSkillNeeded;
    public static int       wagonWeightGrams;
    public static double    wagonMinSkill;

    private static float    carpetMaxSpeed;
    private static float    carpetMaxDepth;
    private static float    carpetMaxHeightDiff;
    public static float     carpetDifficulty;
    private static float    carpetSkillNeeded;
    public static int       carpetWeightGrams;
    public static double    carpetMinSkill;

    private static boolean  debug;

    static{
        logger = Logger.getLogger(Initiator.class.getName());
        wagons = false;
        magicCarpets = false;
        towers = false;
        flags = false;
        tents = false;
        pavilions = false;
        banners = false;
        wagonMaxSpeed = 1.0f;
        wagonMaxDepth = -0.07f;
        wagonMaxHeightDiff = 0.07f;
        wagonDifficulty = 70.0F;
        wagonMaxAllowedLoadDistance = 4;
        wagonSkillNeeded = 21.0f;
        wagonWeightGrams = 240000;
        wagonMinSkill = 40.0d;
        carpetMaxSpeed = 2.0f;
        carpetMaxDepth = 0.07f;
        carpetMaxHeightDiff = 0.07f;
        carpetDifficulty = 80.0f;
        carpetSkillNeeded = 21.0f;
        carpetWeightGrams = 4000;
        carpetMinSkill = 40.0d;
        debug = false;
    }

    @Override
    public void configure(Properties properties) {
        wagons = Boolean.valueOf(properties.getProperty("wagons", String.valueOf(Initiator.wagons)));
        magicCarpets = Boolean.valueOf(properties.getProperty("magicCarpets", String.valueOf(Initiator.magicCarpets)));
        flags = Boolean.valueOf(properties.getProperty("flags", String.valueOf(Initiator.flags)));
        towers = Boolean.valueOf(properties.getProperty("towers", String.valueOf(Initiator.towers)));
        tents = Boolean.valueOf(properties.getProperty("tents", String.valueOf(Initiator.tents)));
        banners = Boolean.valueOf(properties.getProperty("banners", String.valueOf(Initiator.banners)));
        pavilions = Boolean.valueOf(properties.getProperty("pavilions", String.valueOf(Initiator.pavilions)));
        wagonMaxSpeed = Float.parseFloat(properties.getProperty("wagonMaxSpeed", String.valueOf(Initiator.wagonMaxSpeed)));
        wagonMaxDepth = Float.parseFloat(properties.getProperty("wagonMaxDepth", String.valueOf(Initiator.wagonMaxDepth)));
        wagonMaxHeightDiff = Float.parseFloat(properties.getProperty("wagonMaxHeightDiff", String.valueOf(Initiator.wagonMaxHeightDiff)));
        wagonDifficulty = Float.parseFloat(properties.getProperty("wagonDifficulty", String.valueOf(Initiator.wagonDifficulty)));
        wagonMaxAllowedLoadDistance = Integer.parseInt(properties.getProperty("wagonMaxAllowedLoadDistance", String.valueOf(Initiator.wagonMaxAllowedLoadDistance)));
        wagonSkillNeeded = Float.parseFloat(properties.getProperty("wagonSkillNeeded", String.valueOf(Initiator.wagonSkillNeeded)));
        wagonMinSkill = Double.parseDouble(properties.getProperty("wagonMinSkill", String.valueOf(Initiator.wagonMinSkill)));
        wagonWeightGrams = Integer.parseInt(properties.getProperty("wagonWeightGrams", String.valueOf(Initiator.wagonWeightGrams)));
        carpetMaxSpeed = Float.parseFloat(properties.getProperty("carpetMaxSpeed", String.valueOf(Initiator.carpetMaxSpeed)));
        carpetMaxDepth = Float.parseFloat(properties.getProperty("carpetMaxDepth", String.valueOf(Initiator.carpetMaxDepth)));
        carpetMaxHeightDiff = Float.parseFloat(properties.getProperty("carpetMaxHeightDiff", String.valueOf(Initiator.carpetMaxHeightDiff)));
        carpetDifficulty = Float.parseFloat(properties.getProperty("carpetDifficulty", String.valueOf(Initiator.carpetDifficulty)));
        carpetSkillNeeded = Float.parseFloat(properties.getProperty("carpetSkillNeeded", String.valueOf(Initiator.carpetSkillNeeded)));
        carpetMinSkill = Double.parseDouble(properties.getProperty("carpetMinSkill", String.valueOf(Initiator.carpetMinSkill)));
        carpetWeightGrams = Integer.parseInt(properties.getProperty("carpetWeightGrams", String.valueOf(Initiator.carpetWeightGrams)));
        debug = Boolean.valueOf(properties.getProperty("debug", String.valueOf(debug)));

        debug("KingdomWagons: " + wagons);
        if (wagons) { debug("Initializing wagon hooks");
            registerWagonHook();
            registerWagonManageHook();
        }
        debug("MagicCarpets: " + magicCarpets);
        if (magicCarpets) { debug("Initializing magic carpet hooks");
            registerCarpetHook();
            //registerCarpetManageHook();
        }
    }

    public static void registerWagonManageHook() {
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
                            for (int id : WagonFactory.wagonList) {
                                if (id == itemId) {
                                    debug("Adding manage permissions");
                                    original.add(Actions.actionEntrys[Actions.MANAGE_WAGON]);
                                }
                            }
                        }
                        if (item.maySeeHistory(performer)) {
                            int itemId = item.getTemplateId();
                            for (int id : WagonFactory.wagonList) {
                                if (id == itemId) {
                                    original.add(new ActionEntry(Actions.SHOW_HISTORY_FOR_OBJECT, "History of Wagon", "viewing"));
                                }
                            }
                        }
                        return original;
                    });
        }
        catch (Exception e) {
            debug("Permission hook for wagon: " + e.toString());
        }
    }

    public static void registerWagonHook() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.Vehicle")
            };
            CtClass output = CtPrimitiveType.voidType;
            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.Vehicles", "setSettingsForVehicle",
                    Descriptor.ofMethod(output, input), () -> (proxy, method, args) -> {
                        debug("Adding vehicle configuration for wagons");
                        Item item = (Item) args[0];
                        int templateId = item.getTemplateId();
                        for (int i : WagonFactory.wagonList) {
                            if (i == templateId) {
                                Vehicle vehicle = (Vehicle) args[1];
                                VehicleFacadeImpl vehfacade = new VehicleFacadeImpl(vehicle);
                                if (Features.Feature.WAGON_PASSENGER.isEnabled()) {
                                    vehfacade.createPassengerSeats(1);
                                } else {
                                    vehfacade.createPassengerSeats(0);
                                }
                                vehfacade.setPilotName("driver");
                                vehfacade.setCreature(false);
                                vehfacade.setEmbarkString("ride");
                                vehfacade.setEmbarksString("rides");
                                vehicle.name = item.getName();
                                vehicle.setSeatFightMod(0, 0.9f, 0.3f);
                                vehicle.setSeatOffset(0, 0.0f, 0.0f, 0.0f, 1.453f);
                                if (Features.Feature.WAGON_PASSENGER.isEnabled()) {
                                    vehicle.setSeatFightMod(1, 1.0f, 0.4f);
                                    vehicle.setSeatOffset(1, 4.05f, 0.0f, 0.84f);
                                }
                                vehicle.maxHeightDiff = wagonMaxHeightDiff;
                                vehicle.maxDepth = wagonMaxDepth;
                                vehicle.skillNeeded = wagonSkillNeeded;
                                vehfacade.setMaxSpeed(wagonMaxSpeed);
                                vehicle.commandType = 2;
                                SeatsFacadeImpl seatfacad = new SeatsFacadeImpl();
                                final Seat[] hitches = {seatfacad.CreateSeat((byte) 2), seatfacad.CreateSeat((byte) 2), seatfacad.CreateSeat((byte) 2), seatfacad.CreateSeat((byte) 2)};
                                hitches[0].offx = -2.0f;
                                hitches[0].offy = -1.0f;
                                hitches[1].offx = -2.0f;
                                hitches[1].offy = 1.0f;
                                hitches[2].offx = -5.0f;
                                hitches[2].offy = -1.0f;
                                hitches[3].offx = -5.0f;
                                hitches[3].offy = 1.0f;
                                vehicle.addHitchSeats(hitches);
                                vehicle.setMaxAllowedLoadDistance(wagonMaxAllowedLoadDistance);
                                return null;
                            }
                        }
                        return method.invoke(proxy, args);
                    });
        } catch (NotFoundException e) {
            debug("Wagon hook: " + e.toString());
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
                                    vehicle.maxHeightDiff = carpetMaxHeightDiff;
                                    vehicle.maxDepth = carpetMaxDepth;
                                    vehicle.skillNeeded = carpetSkillNeeded;
                                    vehfacade.setMaxSpeed(carpetMaxSpeed);
                                    vehicle.commandType = 2;
                                    return null;
                                }
                            }
                            return method.invoke(proxy,args);
                        });
            } catch (NotFoundException e) {
                debug("Magic carpet hook: " + e.toString());
            }
        }

    public static void debug(String msg) {
        if (debug) { logger.info(msg); }
    }

    @Override
    public void onItemTemplatesCreated() {
        WagonFactory.addAllWagons();
        TowerFactory.addAllTowers();
        MilitaryTentFactory.addAllTents();
        PavilionFactory.addAllPavilions();
        FlagFactory.addAllFlags();
        BannerFactory.addAllBanners();
        MagicCarpetFactory.addAllCarpets();
    }

    public String getVersion() {
        return "v1.7";
    }

}
