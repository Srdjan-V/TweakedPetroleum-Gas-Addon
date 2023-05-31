package io.github.srdjanv.tweakedpetroleumgas.common.compat.crafttweaker;


import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import io.github.srdjanv.tweakedlib.api.powertier.PowerTierHandler;
import io.github.srdjanv.tweakedpetroleum.api.crafting.TweakedPumpjackHandler;
import io.github.srdjanv.tweakedpetroleum.api.mixins.IReservoirType;
import io.github.srdjanv.tweakedpetroleum.util.ReservoirValidation;
import mekanism.common.integration.crafttweaker.gas.IGasStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@SuppressWarnings("unused")
@ZenClass("mods.TweakedPetroleum.TweakedGasReservoir")
@ZenRegister
public class TweakedGasReservoir {

    @ZenMethod
    public static void registerGasReservoir(String name, IGasStack gas, int minSize, int maxSize, int replenishRate, int pumpSpeed, int weight, int powerTier,
                                            @Optional int[] dimBlacklist, @Optional int[] dimWhitelist, @Optional String[] biomeBlacklist, @Optional String[] biomeWhitelist) {


        IReservoirType res;
        if (ReservoirValidation.validateReservoir(name, TweakedPumpjackHandler.ReservoirContent.GAS, gas,
                minSize, maxSize, replenishRate, pumpSpeed, weight, powerTier,
                biomeBlacklist, biomeWhitelist)) {
            res = TweakedPumpjackHandler.addTweakedReservoir(name, gas.getName(), minSize, maxSize, replenishRate, pumpSpeed, weight, powerTier);
        } else {
            if (name != null && !name.isEmpty()) {
                CraftTweakerAPI.logError(String.format("Added dummy gas reservoir: %s", name));
                TweakedPumpjackHandler.addTweakedReservoir(name, "water", 0, 10, 0, 10, 0, PowerTierHandler.getFallbackPowerTier().getId());
            }
            return;
        }

        res.setReservoirContent(TweakedPumpjackHandler.ReservoirContent.GAS);
        res.setDimensionBlacklist(dimBlacklist);
        res.setDimensionWhitelist(dimWhitelist);
        res.setBiomeBlacklist(biomeBlacklist);
        res.setBiomeWhitelist(biomeWhitelist);

        CraftTweakerAPI.logInfo("Added Gas Reservoir Type: " + name);
    }

    @ZenMethod
    public static void registerGasReservoirWithDrainChance(String name, IGasStack gas, int minSize, int maxSize, int replenishRate, int pumpSpeed, float drainChance, int weight, int powerTier,
                                                           @Optional int[] dimBlacklist, @Optional int[] dimWhitelist, @Optional String[] biomeBlacklist, @Optional String[] biomeWhitelist) {

        IReservoirType res;
        if (ReservoirValidation.validateReservoir(name, TweakedPumpjackHandler.ReservoirContent.GAS, gas,
                minSize, maxSize, replenishRate, pumpSpeed, weight, powerTier, drainChance,
                biomeBlacklist, biomeWhitelist)) {
            res = TweakedPumpjackHandler.addTweakedReservoir(name, gas.getName(), minSize, maxSize, replenishRate, pumpSpeed, weight, powerTier);
        } else {
            if (name != null && !name.isEmpty()) {
                CraftTweakerAPI.logError(String.format("Added dummy gas reservoir: %s", name));
                TweakedPumpjackHandler.addTweakedReservoir(name, "water", 0, 10, 0, 10, 0, PowerTierHandler.getFallbackPowerTier().getId());
            }
            return;
        }

        res.setReservoirContent(TweakedPumpjackHandler.ReservoirContent.GAS);
        res.setDrainChance(drainChance);
        if (dimBlacklist != null) res.setDimensionBlacklist(dimBlacklist);
        if (dimWhitelist != null) res.setDimensionWhitelist(dimWhitelist);
        if (biomeBlacklist != null) res.setBiomeBlacklist(biomeBlacklist);
        if (biomeWhitelist != null) res.setBiomeWhitelist(biomeWhitelist);

        CraftTweakerAPI.logInfo("Added Gas Reservoir Type: " + name);

    }

}
