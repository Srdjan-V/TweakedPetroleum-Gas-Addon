package io.github.srdjanv.tweakedpetroleumgas.common.compat.groovy;

import com.cleanroommc.groovyscript.api.GroovyLog;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import io.github.srdjanv.tweakedpetroleum.api.crafting.TweakedPumpjackHandler;
import io.github.srdjanv.tweakedpetroleum.common.compat.groovy.VirtualizedReservoirRegistry;
import io.github.srdjanv.tweakedpetroleum.util.ReservoirValidation;
import io.github.srdjanv.tweakedpetroleumgas.api.mixins.ITweakedGasReservoirType;
import mekanism.api.gas.GasStack;

import java.util.function.BiFunction;

public class GasReservoir extends VirtualizedReservoirRegistry<
        ITweakedGasReservoirType,
        GasReservoir.GasReservoirBuilder,
        GasReservoir.GasReservoirWrapper> {


    public GasReservoir() {
        super("Gas");
    }

    @Override protected BiFunction<PumpjackHandler.ReservoirType, Integer, GasReservoirWrapper> getReservoirTypeWrapperFunction() {
        return GasReservoirWrapper::new;
    }

    @Override public GasReservoirBuilder recipeBuilder() {
        return new GasReservoirBuilder();
    }

    public class GasReservoirBuilder extends ReservoirBuilder<GasReservoirWrapper> {
        protected GasStack gas;

        public GasReservoirBuilder gas(GasStack gas) {
            this.gas = gas;
            return this;
        }

        @Override
        public boolean validate() {
            GroovyLog.Msg msg = GroovyLog.msg("Error adding custom gas reservoir").error();
            ReservoirValidation.validateReservoir(msg::add, name, gas, minSize, maxSize, replenishRate, pumpSpeed, weight, powerTier, drainChance.floatValue(),
                    biomeBlacklist == null ? null : biomeBlacklist.toArray(new String[]{}), biomeWhitelist == null ? null : biomeWhitelist.toArray(new String[]{}));

            return !msg.postIfNotEmpty();
        }

        @SuppressWarnings("UnreachableCode") @Override
        public GasReservoirWrapper register() {
            if (!validate()) return null;
            ITweakedGasReservoirType res = (ITweakedGasReservoirType)
                    new PumpjackHandler.ReservoirType(name, gas.getGas().getName(), minSize, maxSize, replenishRate);

            res.setDrainChance(drainChance.floatValue());
            res.setPumpSpeed(pumpSpeed);
            res.setPowerTier(powerTier);
            res.setReservoirContent(TweakedPumpjackHandler.ReservoirContent.GAS);
            if (dimBlacklist != null) res.setDimensionBlacklist(dimBlacklist.stream().mapToInt(Integer::intValue).toArray());
            if (dimWhitelist != null) res.setDimensionWhitelist(dimWhitelist.stream().mapToInt(Integer::intValue).toArray());
            if (biomeBlacklist != null) res.setBiomeBlacklist(biomeBlacklist.toArray(new String[]{}));
            if (biomeWhitelist != null) res.setBiomeWhitelist(biomeWhitelist.toArray(new String[]{}));

            GasReservoirWrapper wrapper = new GasReservoirWrapper(res, weight);
            add(wrapper);
            return wrapper;
        }
    }

    public class GasReservoirWrapper extends ReservoirWrapper<ITweakedGasReservoirType, GasReservoirBuilder> {
        private final ITweakedGasReservoirType reservoirType;

        public GasReservoirWrapper(ITweakedGasReservoirType reservoirType, int weight) {
            this((PumpjackHandler.ReservoirType) reservoirType, weight);
        }

        public GasReservoirWrapper(PumpjackHandler.ReservoirType reservoirType, int weight) {
            super(GasReservoir.this::recipeBuilder, reservoirType, weight);
            this.reservoirType = (ITweakedGasReservoirType) reservoirType;
        }

        @Override
        public ITweakedGasReservoirType getRealReservoirType() {
            return reservoirType;
        }

        @Override public GasReservoirBuilder toBuilder() {
            remove(this);
            GasReservoirBuilder builder = super.toBuilder();
            builder.gas(new GasStack(getRealReservoirType().getGas(), 1000));
            return builder;
        }

    }

}
