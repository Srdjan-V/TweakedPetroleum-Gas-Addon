package srki2k.tweakedpetroleumgas.util.groovy;

import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import mekanism.api.gas.GasStack;
import srki2k.tweakedpetroleum.api.util.IReservoirType;
import srki2k.tweakedpetroleum.util.groovy.GroovyReservoirValidator;
import srki2k.tweakedpetroleum.util.groovy.abstractclass.AbstractGroovyReservoirWrapper;
import srki2k.tweakedpetroleumgas.api.util.IGasReservoirType;

public class GroovyGasReservoirWrapper extends AbstractGroovyReservoirWrapper<GroovyGasReservoirWrapper> {

    public GroovyGasReservoirWrapper(IReservoirType reservoirType, int weight) {
        super(reservoirType, weight);
    }

    public GroovyGasReservoirWrapper(PumpjackHandler.ReservoirType reservoirType, int weight) {
        super(reservoirType, weight);
    }

    public GroovyGasReservoirWrapper setGas(GasStack gasStack) {
        GroovyReservoirValidator.validateGasStack(groovyLogMsg, innerReservoirWrapper.getReservoirType().name, gasStack);
        if (groovyLogMsg.hasSubMessages()) {
            return this;
        }

        ((IGasReservoirType) getMutableObject().getReservoirType()).setGas(gasStack.getGas());
        return this;
    }

}
