package io.github.srdjanv.tweakedpetroleumgas.api.mixins;

import io.github.srdjanv.tweakedpetroleum.api.mixins.ITweakedPetReservoirTypeGetters;
import io.github.srdjanv.tweakedpetroleum.api.mixins.ITweakedPetReservoirTypeSetters;
import mekanism.api.gas.Gas;

public interface ITweakedGasReservoirTypeGetters extends ITweakedPetReservoirTypeGetters {
    Gas getGas();
}
