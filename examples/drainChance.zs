import mods.TweakedPetroleum.TweakedReservoir;
import mods.TweakedPetroleum.TweakedGasReservoir;

/*
Method Syntax
TweakedReservoir.registerPowerUsage(int tier, int capacity, int rft)
*/
    TweakedReservoir.registerPowerUsage(0, 16000, 1024);


/*
Method Syntax
TweakedGasReservoir.registerGasReservoirWithDrainChance(String name, IGasStack gas, int minSize, int maxSize, int replenishRate, int pumpSpeed, float drainChance, int weight, int powerTier,
    int[] dimBlacklist, int[] dimWhitelist, String[] biomeBlacklist, String[] biomeWhitelist)
*/

    TweakedGasReservoir.registerGasReservoirWithDrainChance("Osmium Slurry", <gas:osmium>, 2500000, 15000000, 6, 25, 0.5, 40, 0,
        [], [0], [], []);