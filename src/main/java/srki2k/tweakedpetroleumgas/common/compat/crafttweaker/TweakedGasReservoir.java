package srki2k.tweakedpetroleumgas.common.compat.crafttweaker;


import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import mekanism.common.integration.crafttweaker.gas.IGasStack;
import srki2k.tweakedpetroleum.api.crafting.TweakedPumpjackHandler;
import srki2k.tweakedpetroleum.api.ihelpers.IReservoirType;
import srki2k.tweakedpetroleum.util.crafttweaker.ReservoirValidation;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@ZenClass("mods.TweakedPetroleum.TweakedGasReservoir")
@ZenRegister
public class TweakedGasReservoir {

    @ZenMethod
    public static void registerGasReservoir(String name, IGasStack gas, int minSize, int maxSize, int replenishRate, int pumpSpeed, int weight, int powerTier,
                                            @Optional int[] dimBlacklist, @Optional int[] dimWhitelist, @Optional String[] biomeBlacklist, @Optional String[] biomeWhitelist) {


        List<String> biomeBlacklistList = new ArrayList<>();
        List<String> biomeWhitelistList = new ArrayList<>();

        ReservoirValidation.validateReservoir(name, minSize, maxSize, replenishRate, pumpSpeed, weight, powerTier,
                biomeBlacklist, biomeWhitelist,
                biomeBlacklistList, biomeWhitelistList);


        IReservoirType res = TweakedPumpjackHandler.addTweakedReservoir(name, gas.getName(), minSize, maxSize, replenishRate, pumpSpeed, weight, powerTier);

        res.setReservoirContent(TweakedPumpjackHandler.ReservoirContent.GAS);
        res.setDimensionBlacklist(dimBlacklist);
        res.setDimensionWhitelist(dimWhitelist);
        res.setBiomeBlacklist(biomeBlacklistList.toArray(new String[0]));
        res.setBiomeWhitelist(biomeWhitelistList.toArray(new String[0]));

        CraftTweakerAPI.logInfo("Added Gas Reservoir Type: " + name);

    }

    @ZenMethod
    public static void registerGasReservoirWithDrainChance(String name, IGasStack gas, int minSize, int maxSize, int replenishRate, int pumpSpeed, float drainChance, int weight, int powerTier,
                                                           @Optional int[] dimBlacklist, @Optional int[] dimWhitelist, @Optional String[] biomeBlacklist, @Optional String[] biomeWhitelist) {

        List<String> biomeBlacklistList = new ArrayList<>();
        List<String> biomeWhitelistList = new ArrayList<>();

        ReservoirValidation.validateReservoir(name, minSize, maxSize, replenishRate, pumpSpeed, weight, powerTier, drainChance,
                biomeBlacklist, biomeWhitelist,
                biomeBlacklistList, biomeWhitelistList);


        IReservoirType res = TweakedPumpjackHandler.addTweakedReservoir(name, gas.getName(), minSize, maxSize, replenishRate, pumpSpeed, weight, powerTier);

        res.setReservoirContent(TweakedPumpjackHandler.ReservoirContent.GAS);
        res.setDrainChance(drainChance);
        res.setDimensionBlacklist(dimBlacklist);
        res.setDimensionWhitelist(dimWhitelist);
        res.setBiomeBlacklist(biomeBlacklistList.toArray(new String[0]));
        res.setBiomeWhitelist(biomeWhitelistList.toArray(new String[0]));

        CraftTweakerAPI.logInfo("Added Gas Reservoir Type: " + name);

    }

}
