package srki2k.tweakedpetroleumgas.common.compat.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import mekanism.api.gas.GasStack;
import srki2k.tweakedpetroleum.api.crafting.TweakedPumpjackHandler;
import srki2k.tweakedpetroleum.api.ihelpers.IReservoirType;
import srki2k.tweakedpetroleum.util.groovy.AbstractReservoirBuilder;
import srki2k.tweakedpetroleum.util.groovy.AbstractVirtualizedReservoirRegistry;
import srki2k.tweakedpetroleum.util.groovy.GroovyReservoirValidator;
import srki2k.tweakedpetroleum.util.groovy.GroovyReservoirWrapper;

@SuppressWarnings("unused")
public class TweakedGroovyGasReservoir extends AbstractVirtualizedReservoirRegistry<TweakedGroovyGasReservoir, TweakedGroovyGasReservoir.GasReservoirBuilder> {

    public TweakedGroovyGasReservoir() {
        super("GasReservoir", "GasReservoir");
    }

    private static TweakedGroovyGasReservoir instance;

    @GroovyBlacklist
    public static TweakedGroovyGasReservoir init() {
        return instance = new TweakedGroovyGasReservoir();
    }

    @Override
    public TweakedGroovyGasReservoir getInstance() {
        return instance;
    }

    @Override
    public GasReservoirBuilder recipeBuilder() {
        return new GasReservoirBuilder();
    }

    public static class GasReservoirBuilder extends AbstractReservoirBuilder<GasReservoirBuilder> {

        public GasReservoirBuilder Gas(GasStack Gas) {
            ingredient = (IIngredient) Gas;
            return this;
        }

        @Override
        public boolean validate() {
            GroovyLog.Msg msg = GroovyLog.msg("Error adding custom gas reservoir").error();
            GroovyReservoirValidator.validateGasGroovyReservoir(msg, name, ingredient, minSize, maxSize, replenishRate, pumpSpeed, weight, powerTier, drainChance,
                    dimBlacklist, dimWhitelist, biomeBlacklist, biomeWhitelist);

            return !msg.postIfNotEmpty();
        }

        @Override
        public GroovyReservoirWrapper register() {
            if (validate()) {
                IReservoirType res = (IReservoirType) new PumpjackHandler.ReservoirType(name, ((GasStack) ingredient).getGas().getName(), minSize, maxSize, replenishRate);
                res.setReservoirContent(TweakedPumpjackHandler.ReservoirContent.GAS);

                commonRegister(res);

                GroovyReservoirWrapper groovyReservoirWrapper = new GroovyReservoirWrapper(res, weight);
                instance.add(groovyReservoirWrapper);
                return groovyReservoirWrapper;
            }

            return null;
        }

    }
}
