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
import org.gotti.wurmunlimited.modloader.interfaces.ServerStartedListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.requiem.mods.kingdomitems.util.SeatsFacadeImpl;
import org.requiem.mods.kingdomitems.util.VehicleFacadeImpl;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Adds various kingdom items.
 */
/* Default wagon settings
            vehicle.maxHeightDiff = 0.04F;
            vehicle.maxDepth = -0.7F;
            vehicle.skillNeeded = 21.0F;
            vehicle.setMaxSpeed(0.7F);
 */

public class AddKingdomItems implements WurmServerMod, Configurable, ItemTemplatesCreatedListener, ServerStartedListener {

    private boolean wagons;
    private boolean magicCarpets;
    private boolean towers;
    private boolean flags;
    private boolean tents;
    private boolean pavilions;
    private boolean banners;
    private float setMaxSpeed;
    private float setMaxDepth;
    private float setMaxHeight;
    private float setMaxHeightDiff;
    public static float difficulty;
    private int setMaxAllowedLoadDistance;
    private int skillNeeded;
    public static int weightGrams;
    public static double minSkill;
    private static boolean debug;
    private static Logger logger = Logger.getLogger(AddKingdomItems.class.getName());

    @Override
    public void configure(Properties properties) {
        wagons = Boolean.valueOf(properties.getProperty("wagons"));
        magicCarpets = Boolean.valueOf(properties.getProperty("MagicCarpets"));
        flags = Boolean.valueOf(properties.getProperty("flags"));
        towers = Boolean.valueOf(properties.getProperty("towers"));
        tents = Boolean.valueOf(properties.getProperty("tents"));
        banners = Boolean.valueOf(properties.getProperty("banners"));
        pavilions = Boolean.valueOf(properties.getProperty("pavilions"));
        setMaxSpeed = Float.parseFloat(properties.getProperty("setMaxSpeed", Float.toString(setMaxSpeed)));
        setMaxDepth = Float.parseFloat(properties.getProperty("setMaxDepth", Float.toString(setMaxDepth)));
        setMaxHeight = Float.parseFloat(properties.getProperty("setMaxHeight", Float.toString(setMaxHeight)));
        setMaxHeightDiff = Float.parseFloat(properties.getProperty("setMaxHeightDiff", Float.toString(setMaxHeightDiff)));
        difficulty = Float.parseFloat(properties.getProperty("difficulty", Float.toString(difficulty)));
        setMaxAllowedLoadDistance = Integer.parseInt(properties.getProperty("setMaxAllowedLoadDistance", Integer.toString(setMaxAllowedLoadDistance)));
        skillNeeded = Integer.parseInt(properties.getProperty("skillNeeded", Integer.toString(skillNeeded)));
        minSkill = Double.parseDouble(properties.getProperty("minSkill", Double.toString(minSkill)));
        weightGrams = Integer.parseInt(properties.getProperty("weightGrams", Integer.toString(weightGrams)));
        debug = Boolean.valueOf(properties.getProperty("debug", String.valueOf(true)));

        debug("KingdomWagons: " + wagons);

        if (wagons) { debug("Initializing wagon hooks");
            registerWagonHook();
            registerManageHook();
            MagicCarpetFactory.registerCarpetHook();
            MagicCarpetFactory.registerCarpetManageHook();
        }
    }

    public static void registerManageHook() {
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
            debug("Permission hook: " + e.toString());
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
                        for (int i: WagonFactory.wagonList) {
                            if (i==templateId) {
                                Vehicle vehicle = (Vehicle) args[1];
                                VehicleFacadeImpl vehfacade = new VehicleFacadeImpl(vehicle);
                                if (Features.Feature.WAGON_PASSENGER.isEnabled()) {
                                    vehfacade.createPassengerSeats(1);
                                }
                                else {
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
                                vehicle.maxHeightDiff = 0.04f;
                                vehicle.maxDepth = -0.7f;
                                vehicle.skillNeeded = 21.0f;
                                vehfacade.setMaxSpeed(1.0f);
                                vehicle.commandType = 2;
                                SeatsFacadeImpl seatfacad= new SeatsFacadeImpl();

                                final Seat[] hitches = { seatfacad.CreateSeat((byte)2),seatfacad.CreateSeat((byte)2),seatfacad.CreateSeat((byte)2),seatfacad.CreateSeat((byte)2) };

                                hitches[0].offx = -2.0f;
                                hitches[0].offy = -1.0f;
                                hitches[1].offx = -2.0f;
                                hitches[1].offy = 1.0f;
                                hitches[2].offx = -5.0f;
                                hitches[2].offy = -1.0f;
                                hitches[3].offx = -5.0f;
                                hitches[3].offy = 1.0f;
                                vehicle.addHitchSeats(hitches);
                                vehicle.setMaxAllowedLoadDistance(4);
                                return null;
                            }
                        }
                        return method.invoke(proxy,args);
                    });
        } catch (NotFoundException e) {
            debug("Vehicle hook: " + e.toString());
        }
    }

    @Override
    public void init() {
    }

    public static void debug(String msg) {
        if (debug) {
            logger.info(msg);
        }
    }

    @Override
    public void onItemTemplatesCreated() {
        if (wagons) { WagonFactory.addAllWagons(); }
        if (towers) { TowerFactory.addAllTowers(); }
        if (tents) { MilitaryTentFactory.addAllTents(); }
        if (pavilions) { PavilionFactory.addAllPavilions(); }
        if (flags) { FlagFactory.addAllFlags(); }
        if (banners) { BannerFactory.addAllBanners(); }
        if (magicCarpets) { MagicCarpetFactory.addAllCarpets(); }
    }

    @Override
    public void onServerStarted() {
        if (magicCarpets) { MagicCarpetFactory.createCreationEntries(); }
    }

}
