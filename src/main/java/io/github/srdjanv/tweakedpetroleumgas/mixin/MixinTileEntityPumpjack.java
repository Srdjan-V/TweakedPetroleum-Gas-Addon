package io.github.srdjanv.tweakedpetroleumgas.mixin;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import flaxbeard.immersivepetroleum.common.blocks.metal.TileEntityPumpjack;
import io.github.srdjanv.tweakedpetroleumgas.api.mixins.ITweakedGasPumpjackAddons;
import mekanism.api.gas.*;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.util.GasUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import org.spongepowered.asm.mixin.*;
import io.github.srdjanv.tweakedpetroleumgas.api.crafting.TweakedGasPumpjackHandler;

import javax.annotation.Nullable;

@SuppressWarnings("NullableProblems")
@Mixin(value = TileEntityPumpjack.class, remap = false, priority = 950)
public abstract class MixinTileEntityPumpjack extends TileEntityMultiblockMetal<TileEntityPumpjack, IMultiblockRecipe> implements ITweakedGasPumpjackAddons, IGasHandler {

    @Shadow
    public abstract void extractOil(int drained);

    public MixinTileEntityPumpjack(MultiblockHandler.IMultiblock mutliblockInstance, int[] structureDimensions, int energyCapacity, boolean redstoneControl) {
        super(mutliblockInstance, structureDimensions, energyCapacity, redstoneControl);
    }

    @Unique
    @Override
    public boolean caseGas(int consumed, int pumpSpeed, int oilAmnt) {
        this.energyStorage.extractEnergy(consumed, false);

        GasStack out = new GasStack(this.getGas(), Math.min(pumpSpeed, oilAmnt));
        BlockPos outputPos = this.getPos().offset(this.facing, 2).offset(this.facing.rotateY().getOpposite(), 1).offset(EnumFacing.DOWN, 1);
        IGasHandler output = GasUtils.getConnectedAcceptors(outputPos, this.world)[this.facing.rotateY().getOpposite().ordinal()];

        if (output != null) {
            int accepted = output.receiveGas(this.facing.rotateY(), out, false);
            if (accepted > 0) {
                int drained = output.receiveGas(this.facing.rotateY(), new GasStack(out.getGas(), accepted), true);
                this.extractOil(drained);
                out = new GasStack(out.getGas(), out.amount - drained);
            }
        }

        outputPos = this.getPos().offset(this.facing, 2).offset(this.facing.rotateY(), 1).offset(EnumFacing.DOWN, 1);
        output = GasUtils.getConnectedAcceptors(outputPos, this.world)[this.facing.rotateY().ordinal()];

        if (output != null) {
            int accepted = output.receiveGas(this.facing.rotateY().getOpposite(), out, false);
            if (accepted > 0) {
                int drained = output.receiveGas(this.facing.rotateY().getOpposite(), new GasStack(out.getGas(), accepted), true);
                this.extractOil(drained);
            }
        }

        return true;
    }


    @Unique
    @Override
    public Gas getGas() {
        return TweakedGasPumpjackHandler.getGas(
                this.getWorld(), this.getPos().getX() >> 4, this.getPos().getZ() >> 4);

    }

    @Unique
    @Override
    public GasTankInfo[] getAccessibleGasTanks(EnumFacing side) {
        TileEntityPumpjack master = this.master();
        if (master != null) {
            if (this.pos == 9 && (side == null || side == this.facing.rotateY() || side == this.facing.getOpposite().rotateY())) {
                return new GasTank[]{new GasTank(0)};
            }

            if (this.pos == 11 && (side == null || side == this.facing.rotateY() || side == this.facing.getOpposite().rotateY())) {
                return new GasTank[]{new GasTank(0)};
            }
        }

        return NONE;
    }

    @Unique
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY && this.getAccessibleGasTanks(facing).length > 0)
            return true;

        return super.hasCapability(capability, facing);
    }

    @Unique
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY && this.getAccessibleGasTanks(facing).length > 0)
            return (T) this;

        return super.getCapability(capability, facing);
    }

    @Override
    public int receiveGas(EnumFacing side, GasStack resource, boolean doTransfer) {
        return 0;
    }

    @Override
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer) {
        return null;
    }

    @Override
    public boolean canReceiveGas(EnumFacing side, Gas type) {
        return false;
    }

    @Override
    public boolean canDrawGas(EnumFacing side, Gas type) {
        return false;
    }
}
