package srki2k.tweakedpetroleumgas;


import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import srki2k.tweakedlib.util.Constants;
import srki2k.tweakedpetroleumgas.common.compat.groovyscript.TweakedGroovyGasReservoir;

import static srki2k.tweakedpetroleum.common.compat.groovyscript.GroovyScriptCompat.registerTweakedPetroleumAddon;

@Mod(modid = TweakedPetroleumGas.MODID,
        version = TweakedPetroleumGas.VERSION,
        name = "Tweaked Petroleum: Gas Addon",
        dependencies = "required-after:tweakedpetroleum@[@TWEAKEDPETROLEUMVERSION@,);" +
                "required-after:mekanism;")

public class TweakedPetroleumGas {

    public static final String MODID = "tweakedpetroleumgas";
    public static final String VERSION = "@VERSION@";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (Constants.isGroovyScriptLoaded()) {
            registerTweakedPetroleumAddon(TweakedGroovyGasReservoir.init());
        }
    }


}
