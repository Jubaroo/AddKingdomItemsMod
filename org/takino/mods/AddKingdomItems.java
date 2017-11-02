package org.takino.mods;

import com.wurmonline.server.Features;
import com.wurmonline.server.behaviours.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.factories.BannerFactory;
import com.wurmonline.server.items.factories.FlagFactory;
import com.wurmonline.server.items.factories.WagonFactory;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.ItemTemplatesCreatedListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Adds various kingdom items.
 */
public class AddKingdomItems implements WurmServerMod, Configurable, ItemTemplatesCreatedListener {

    private boolean wagons;

    private boolean tabards;
    private boolean towers;
    private boolean flags;

    private static boolean debug;
    private static Logger logger = Logger.getLogger(AddKingdomItems.class.getName());
    @Override
    public void configure(Properties properties) {
        wagons = Boolean.valueOf(properties.getProperty("wagons"));
        tabards = Boolean.valueOf(properties.getProperty("tabards"));
        flags = Boolean.valueOf(properties.getProperty("flags"));
        towers = Boolean.valueOf(properties.getProperty("towers"));
        debug = Boolean.valueOf(properties.getProperty("debug", String.valueOf(true)));
        debug("Wagons: " + wagons);
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
                    Descriptor.ofMethod(output, input), new InvocationHandlerFactory() {
                        @Override
                        public InvocationHandler createInvocationHandler() {
                            return new InvocationHandler() {
                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
                                }

                            };
                        }
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
                    Descriptor.ofMethod(output, input), new InvocationHandlerFactory() {
                        @Override
                        public InvocationHandler createInvocationHandler() {
                            return new InvocationHandler() {
                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
                                            vehicle.skillNeeded = 21.0F;
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
                                            vehicle.setMaxAllowedLoadDistance(4);
                                            BehaviourAccessor.setMaxSpeed(vehicle,1.2f);
                                            BehaviourAccessor.setMaxDepth(vehicle, -1.5f);
                                            BehaviourAccessor.setMaxHeight(vehicle,2500.0f);
                                            BehaviourAccessor.setMaxHeightDiff(vehicle,0.06f);
                                            return null;
                                        }
                                    }
                                    return method.invoke(proxy,args);
                                }
                            };
                        }
                    });
            //setSettingsForVehicle Item Vehicles
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
        if (wagons) {
            WagonFactory.addAllWagons();
        }
        if (flags) {
            BannerFactory.addAllBanners();
            FlagFactory.addAllFlags();
        }
    }
}
