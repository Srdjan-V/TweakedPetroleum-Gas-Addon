package srki2k.tweakedpetroleumgas.api.crafting;

import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import mekanism.api.gas.Gas;
import net.minecraft.world.World;
import srki2k.tweakedpetroleumgas.api.util.IGasReservoirType;

import static flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler.getOilWorldInfo;

public class TweakedGasPumpjackHandler {

    /**
     * Gets the gas of a given chunk
     *
     * @param world      World whose chunk gas to get
     * @param chunkX     Chunk x
     * @param chunkZ     Chunk z
     * @return Returns Gas
     * */
    public static Gas getGas(World world, int chunkX, int chunkZ) {
        if (world.isRemote) {
            return null;
        }

        PumpjackHandler.OilWorldInfo info = getOilWorldInfo(world, chunkX, chunkZ);

        if (info != null && info.getType() != null){
            return ((IGasReservoirType) info.getType()).getGas();
        }

        return null;
    }

}
