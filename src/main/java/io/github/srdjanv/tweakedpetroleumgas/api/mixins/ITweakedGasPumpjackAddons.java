package io.github.srdjanv.tweakedpetroleumgas.api.mixins;

import io.github.srdjanv.tweakedpetroleum.api.mixins.ITweakedPetPumpjackAddons;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasTank;
import net.minecraft.util.EnumFacing;

public interface ITweakedGasPumpjackAddons extends ITweakedPetPumpjackAddons {

    Gas getGas();

    GasTank[] getAccessibleGasTanks(EnumFacing side);

}
