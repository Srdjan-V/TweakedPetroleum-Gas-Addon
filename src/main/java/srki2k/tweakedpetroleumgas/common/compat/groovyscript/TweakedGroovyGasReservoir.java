package srki2k.tweakedpetroleumgas.common.compat.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import mekanism.api.gas.GasStack;
import srki2k.tweakedpetroleum.api.crafting.TweakedPumpjackHandler;
import srki2k.tweakedpetroleum.api.util.IReservoirType;
import srki2k.tweakedpetroleum.util.groovy.GroovyReservoirValidator;
import srki2k.tweakedpetroleum.util.groovy.abstractclass.AbstractReservoirBuilder;
import srki2k.tweakedpetroleum.util.groovy.abstractclass.AbstractVirtualizedReservoirRegistry;
import srki2k.tweakedpetroleumgas.util.groovy.GroovyGasReservoirWrapper;

@SuppressWarnings("unused")
public class TweakedGroovyGasReservoir extends AbstractVirtualizedReservoirRegistry<TweakedGroovyGasReservoir, GroovyGasReservoirWrapper, TweakedGroovyGasReservoir.GasReservoirBuilder> {

    public TweakedGroovyGasReservoir() {
        super("GasReservoir", GroovyGasReservoirWrapper.class, "GasReservoir");
    }

    private static TweakedGroovyGasReservoir instance;

    @GroovyBlacklist
    public static TweakedGroovyGasReservoir init() {
        return instance = new TweakedGroovyGasReservoir();
    }

    @GroovyBlacklist
    public static TweakedGroovyGasReservoir getInstance() {
        return instance;
    }

    @Override
    public GasReservoirBuilder recipeBuilder() {
        return new GasReservoirBuilder();
    }

    public static class GasReservoirBuilder extends AbstractReservoirBuilder<GasReservoirBuilder, GroovyGasReservoirWrapper> {
        protected GasStack gas;
        public GasReservoirBuilder Gas(GasStack gas) {
            this.gas = gas;
            return this;
        }

        @Override
        public boolean validate() {
            GroovyLog.Msg msg = GroovyLog.msg("Error adding custom gas reservoir").error();
            GroovyReservoirValidator.validateGasGroovyReservoir(msg, name, gas, minSize, maxSize, replenishRate, pumpSpeed, weight, powerTier, drainChance,
                    dimBlacklist, dimWhitelist, biomeBlacklist, biomeWhitelist);

            return !msg.postIfNotEmpty();
        }

        @Override
        public GroovyGasReservoirWrapper register() {
            if (validate()) {
                IReservoirType res = (IReservoirType) new PumpjackHandler.ReservoirType(name, gas.getGas().getName(), minSize, maxSize, replenishRate);
                res.setReservoirContent(TweakedPumpjackHandler.ReservoirContent.GAS);

                commonRegister(res);

                GroovyGasReservoirWrapper groovyReservoirWrapper = new GroovyGasReservoirWrapper(res, weight);
                getInstance().add(groovyReservoirWrapper.getInnerReservoirWrapper());
                return groovyReservoirWrapper;
            }

            return null;
        }

    }
}
