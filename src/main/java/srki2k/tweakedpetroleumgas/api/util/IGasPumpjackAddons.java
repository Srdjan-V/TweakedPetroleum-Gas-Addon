package srki2k.tweakedpetroleumgas.api.util;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasTank;
import net.minecraft.util.EnumFacing;
import srki2k.tweakedpetroleum.api.util.IPumpjackAddons;

public interface IGasPumpjackAddons extends IPumpjackAddons {

    Gas getGas();

    GasTank[] getAccessibleGasTanks(EnumFacing side);

}
