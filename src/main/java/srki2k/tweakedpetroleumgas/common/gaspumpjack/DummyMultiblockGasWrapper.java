package srki2k.tweakedpetroleumgas.common.gaspumpjack;


import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasHandler;
import net.minecraft.util.EnumFacing;

public class DummyMultiblockGasWrapper implements IGasHandler {
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
