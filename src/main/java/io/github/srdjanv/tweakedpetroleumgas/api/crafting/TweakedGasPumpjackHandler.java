package io.github.srdjanv.tweakedpetroleumgas.api.crafting;

import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import io.github.srdjanv.tweakedpetroleumgas.api.mixins.IGasReservoirType;
import mekanism.api.gas.Gas;
import net.minecraft.world.World;

import static flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler.getOilWorldInfo;

public class TweakedGasPumpjackHandler {

    /**
     * Gets the gas of a given chunk
     *
     * @param world World whose chunk to drain
     * @param chunkX Chunk x
     * @param chunkZ Chunk z
     * @return Returns Gas
     */
    public static Gas getGas(World world, int chunkX, int chunkZ) {
        if (world.isRemote) {
            return null;
        }

        PumpjackHandler.OilWorldInfo info = getOilWorldInfo(world, chunkX, chunkZ);

        if (info != null && info.getType() != null) {
            return ((IGasReservoirType) info.getType()).getGas();
        }

        return null;
    }

}
