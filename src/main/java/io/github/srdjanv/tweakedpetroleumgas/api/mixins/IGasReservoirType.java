package io.github.srdjanv.tweakedpetroleumgas.api.mixins;

import mekanism.api.gas.Gas;
import io.github.srdjanv.tweakedpetroleum.api.mixins.IReservoirType;

public interface IGasReservoirType extends IReservoirType {

    Gas getGas();

}
