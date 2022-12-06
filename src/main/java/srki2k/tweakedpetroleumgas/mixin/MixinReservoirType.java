package srki2k.tweakedpetroleumgas.mixin;

import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import srki2k.tweakedpetroleumgas.api.util.IGasReservoirType;

@Mixin(value = PumpjackHandler.ReservoirType.class, remap = false)
public abstract class MixinReservoirType implements IGasReservoirType {

    @Shadow
    public String fluid;

    @Unique
    private Gas gas;

    @Unique
    @Override
    public Gas getGas() {
        if (fluid == null) {
            return null;
        }

        if (gas == null) {
            gas = GasRegistry.getGas(fluid);
        }
        return gas;
    }

    @Unique
    @Override
    public void setGas(Gas gas) {
        fluid = gas.getName();
        this.gas = gas;
    }

}
