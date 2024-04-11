package io.github.srdjanv.tweakedpetroleumgas.api.mixins;

import io.github.srdjanv.tweakedpetroleum.api.mixins.ITweakedPetPumpjackAddons;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasTank;
import mekanism.api.gas.GasTankInfo;
import net.minecraft.util.EnumFacing;

public interface ITweakedGasPumpjackAddons extends ITweakedPetPumpjackAddons {

    Gas getGas();

    GasTankInfo[] getAccessibleGasTanks(EnumFacing side);

}
