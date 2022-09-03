package srki2k.tweakedpetroleumgas.mixin;

import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import srki2k.tweakedpetroleumgas.api.util.IGasReservoirType;

@Mixin(PumpjackHandler.ReservoirType.class)
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
