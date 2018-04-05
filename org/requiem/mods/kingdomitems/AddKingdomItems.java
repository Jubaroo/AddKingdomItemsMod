package org.requiem.mods.kingdomitems;

import com.wurmonline.server.Features;
import com.wurmonline.server.behaviours.*;
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
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviours;

import java.lang.reflect.Method;
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

public class AddKingdomItems implements WurmServerMod, Configurable, ItemTemplatesCreatedListener {

    private boolean wagons;
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
        if (wagons) {
            debug("Initializing wagon hooks");
            registerWagonHook();
            registerManageHook();
        }
    }

    private void registerManageHook() {
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
                                    original.add(Actions.actionEntrys[669]);
                                }
                            }
                        }
                        if (item.maySeeHistory(performer)) {
                            int itemId = item.getTemplateId();
                            for (int id : WagonFactory.wagonList) {
                                if (id == itemId) {
                                    original.add(new ActionEntry((short)691, "History of Wagon", "viewing"));
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

    private void registerWagonHook() {
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
                                Seat[] hitches;
                                Method passenger = vehicle.getClass().getDeclaredMethod("createPassengerSeats", int.class);
                                passenger.setAccessible(true);
                                if(Features.Feature.WAGON_PASSENGER.isEnabled()) {
                                    passenger.invoke(vehicle,1);
                                    //vehicle.createPassengerSeats(1);
                                } else {
                                    passenger.invoke(vehicle,0);
                                }
                                BehaviourAccessor.setPilotName(vehicle,"driver");
                                BehaviourAccessor.setEmbarkString(vehicle,"ride");
                                vehicle.name = item.getName();
                                vehicle.setSeatFightMod(0, 0.9F, 0.3F);
                                vehicle.setSeatOffset(0, 0.0F, 0.0F, 0.0F, 1.453F);
                                if(Features.Feature.WAGON_PASSENGER.isEnabled()) {
                                    vehicle.setSeatFightMod(1, 1.0F, 0.4F);
                                    vehicle.setSeatOffset(1, 4.05F, 0.0F, 0.84F);
                                }
                                vehicle.skillNeeded = skillNeeded;//21.0F
                                vehicle.commandType = 2;
                                hitches = new Seat[]{BehaviourAccessor.getSeat(((byte)2)),
                                        BehaviourAccessor.getSeat((byte)2), BehaviourAccessor.getSeat((byte)2),
                                        BehaviourAccessor.getSeat((byte)2)};
                                hitches[0].offx = -2.0F;
                                hitches[0].offy = -1.0F;
                                hitches[1].offx = -2.0F;
                                hitches[1].offy = 1.0F;
                                hitches[2].offx = -5.0F;
                                hitches[2].offy = -1.0F;
                                hitches[3].offx = -5.0F;
                                hitches[3].offy = 1.0F;
                                vehicle.addHitchSeats(hitches);
                                vehicle.setMaxAllowedLoadDistance(setMaxAllowedLoadDistance);//4
                                BehaviourAccessor.setMaxSpeed(vehicle, setMaxSpeed);
                                BehaviourAccessor.setMaxDepth(vehicle, setMaxDepth);//0.7F
                                BehaviourAccessor.setMaxHeight(vehicle,setMaxHeight);//2.0F
                                BehaviourAccessor.setMaxHeightDiff(vehicle,setMaxHeightDiff);//0.04F
                                return null;
                            }
                        }
                        return method.invoke(proxy,args);
                    });
            //setSettingsForVehicle Item Vehicles
        } catch (NotFoundException e) {

            debug("Vehicle hook: " + e.toString());
        }
    }

    @Override
    public void init() {
        ModVehicleBehaviours.init();
    }

    public static void debug(String msg) {
        if (debug) {
            logger.info(msg);
        }
    }

    @Override
    public void onItemTemplatesCreated() {
        if (wagons) {
            WagonFactory.addAllWagons();
        }
        if (towers) {
            TowerFactory.addAllTowers();
        }
        if (tents) {
            MilitaryTentFactory.addAllTents();
        }
        if (pavilions) {
            PavilionFactory.addAllPavilions();
        }
        if (flags) {
            FlagFactory.addAllFlags();
        }
        if (banners) {
            BannerFactory.addAllBanners();
        }
    }
}
