import mods.TweakedPetroleum.TweakedReservoir;
import mods.TweakedPetroleum.TweakedGasReservoir;


/*
Method Syntax
TweakedReservoir.registerPowerUsage(int tier, int capacity, int rft)
*/

    TweakedReservoir.registerPowerUsage(0, 16000, 1024);



/*
Method Syntax
TweakedGasReservoir.registerGasReservoir(String name, IGasStack gas, int minSize, int maxSize, int replenishRate, int pumpSpeed, int weight, int powerTier,
    int[] dimBlacklist, int[] dimWhitelist, String[] biomeBlacklist, String[] biomeWhitelist)
*/

    TweakedGasReservoir.registerGasReservoir("Hydrogen Gas", <gas:hydrogen>, 2500000, 15000000, 150, 350, 40, 0,
        [], [0], [], []);

    TweakedGasReservoir.registerGasReservoir("Ethylene Gas", <gas:ethene>, 2500000, 15000000, 200, 450, 50, 0,
        [], [0, -1], [], []);

    TweakedGasReservoir.registerGasReservoir("Osmium Slurry", <gas:osmium>, 2500000, 15000000, 150, 250, 30, 0,
        [], [0, 1], [], []);