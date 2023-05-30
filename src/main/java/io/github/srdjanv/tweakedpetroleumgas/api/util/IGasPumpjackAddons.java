package io.github.srdjanv.tweakedpetroleumgas.api.util;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasTank;
import net.minecraft.util.EnumFacing;
import io.github.srdjanv.tweakedpetroleum.api.ihelpers.IPumpjackAddons;

public interface IGasPumpjackAddons extends IPumpjackAddons {

    Gas getGas();

    GasTank[] getAccessibleGasTanks(EnumFacing side);

}
