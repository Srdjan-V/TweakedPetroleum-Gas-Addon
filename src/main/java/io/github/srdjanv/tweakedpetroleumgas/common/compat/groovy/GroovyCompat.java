package io.github.srdjanv.tweakedpetroleumgas.common.compat.groovy;

import io.github.srdjanv.tweakedlib.common.Constants;
import io.github.srdjanv.tweakedlib.common.compat.groovyscript.GroovyScriptRegistry;
import io.github.srdjanv.tweakedpetroleumgas.util.TweakedPetroleumGasInitializer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class GroovyCompat implements TweakedPetroleumGasInitializer {

    @Override public boolean shouldRun() {
        return Constants.isGroovyScriptLoaded();
    }

    @Override public void preInit(FMLPreInitializationEvent event) {
        GroovyScriptRegistry.getRegistry().addRegistry(new GasReservoir());
    }
}
