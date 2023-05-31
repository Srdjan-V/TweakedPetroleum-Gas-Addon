package io.github.srdjanv.tweakedpetroleumgas.api.mixins;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasTank;
import net.minecraft.util.EnumFacing;
import io.github.srdjanv.tweakedpetroleum.api.mixins.IPumpjackAddons;

public interface IGasPumpjackAddons extends IPumpjackAddons {

    Gas getGas();

    GasTank[] getAccessibleGasTanks(EnumFacing side);

}
