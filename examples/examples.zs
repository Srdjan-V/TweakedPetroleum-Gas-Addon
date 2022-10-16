import mods.TweakedPetroleum.TweakedReservoir;
import mods.TweakedPetroleum.TweakedGasReservoir;
import mods.TweakedLib.TweakedPowerTier;

/*
    TweakedReservoir.registerReservoir(String name, ILiquidStack fluid, int minSize, int maxSize, int replenishRate, int pumpSpeed, int weight, int powerTier,
                                        @Optional int[] dimBlacklist, @Optional int[] dimWhitelist, @Optional String[] biomeBlacklist, @Optional String[] biomeWhitelist)

    TweakedPowerTier.registerPowerTier(int capacity, int rft)
*/

    var powerTier = TweakedPowerTier.registerPowerTier(16000, 1024);
    var powerTier2 = TweakedPowerTier.registerPowerTier(160000, 10240);


/*
TweakedGasReservoir.registerGasReservoir(String name, IGasStack gas, int minSize, int maxSize, int replenishRate, int pumpSpeed, int weight, int powerTier,
                                             @Optional int[] dimBlacklist, @Optional int[] dimWhitelist, @Optional String[] biomeBlacklist, @Optional String[] biomeWhitelist)
*/

    TweakedGasReservoir.registerGasReservoir("Hydrogen Gas", <gas:hydrogen>, 2500000, 15000000, 150, 350, 40, powerTier,
        [], [0]);

    TweakedGasReservoir.registerGasReservoir("Ethylene Gas", <gas:ethene>, 2500000, 15000000, 200, 450, 50, powerTier,
        [], [0, -1], [], []);

    TweakedGasReservoir.registerGasReservoir("Osmium Slurry", <gas:osmium>, 2500000, 15000000, 150, 250, 30, powerTier,
        [], [0, 1], []);


/*
    TweakedGasReservoir.registerGasReservoirWithDrainChance(String name, IGasStack gas, int minSize, int maxSize, int replenishRate, int pumpSpeed, float drainChance, int weight, int powerTier,
    int[] dimBlacklist, int[] dimWhitelist, String[] biomeBlacklist, String[] biomeWhitelist)
*/

    TweakedGasReservoir.registerGasReservoirWithDrainChance("Osmium Slurry", <gas:osmium>, 2500000, 15000000, 6, 25, 0.5, 40, powerTier2,
        [], [0], [], []);