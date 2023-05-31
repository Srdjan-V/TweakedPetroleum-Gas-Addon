package io.github.srdjanv.tweakedpetroleumgas.mixin;

import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import io.github.srdjanv.tweakedpetroleumgas.api.mixins.IGasReservoirType;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = PumpjackHandler.ReservoirType.class, remap = false)
public abstract class MixinReservoirType implements IGasReservoirType {

    @Shadow
    public String fluid;

    @Unique
    Gas gas;

    @Override
    public Gas getGas() {

        if (this.fluid == null) {
            return null;
        }

        if (this.gas == null) {
            this.gas = GasRegistry.getGas(fluid);
        }

        return this.gas;

    }
}
