package srki2k.tweakedpetroleumgas;


import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import srki2k.tweakedpetroleum.common.Configs;
import srki2k.tweakedpetroleum.util.errorloggingutil.ErrorLoggingUtil;

@Mod(modid = TweakedPetroleumGas.MODID,
        version = TweakedPetroleumGas.VERSION,
        name = "Tweaked Petroleum: Gas Addon",
        dependencies = "required-after:tweakedpetroleum@[@TWEAKEDPETROLEUMVERSION@,);" +
                "required-after:mekanism;")

public class TweakedPetroleumGas {

    public static final String MODID = "tweakedpetroleumgas";
    public static final String VERSION = "@VERSION@";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (!Configs.TPConfig.StartupScriptChecks.disableAllChecks) {
            ErrorLoggingUtil.getStartupInstance().validateScripts();
        }
    }
}
