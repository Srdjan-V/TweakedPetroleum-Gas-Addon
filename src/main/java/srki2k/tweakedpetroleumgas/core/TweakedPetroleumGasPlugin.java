package srki2k.tweakedpetroleumgas.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.ILateMixinLoader;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TweakedPetroleumGasPlugin implements IFMLLoadingPlugin, ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.tweakedpetroleumgas.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
