package srki2k.tweakedpetroleumgas.client.hei;

import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import srki2k.tweakedlib.util.Constants;
import srki2k.tweakedpetroleum.api.crafting.TweakedPumpjackHandler;
import srki2k.tweakedpetroleum.api.util.IReservoirType;
import srki2k.tweakedpetroleum.util.hei.HEIPumpjackUtil;

import java.util.Collection;
import java.util.stream.Collectors;

@mezz.jei.api.JEIPlugin
public class HEIPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new GasPumpjackCategory());
    }


    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(PumpjackHandler.ReservoirType.class, GasPumpjackWrapper::new, GasPumpjackCategory.UID);
        registry.addRecipes(getRecipes(), GasPumpjackCategory.UID);
        registry.addRecipeCatalyst(HEIPumpjackUtil.getPumpjackCatalyst(), GasPumpjackCategory.UID);
    }

    private Collection<PumpjackHandler.ReservoirType> getRecipes() {
        return PumpjackHandler.reservoirList.keySet().stream().
                filter(reservoirType -> ((IReservoirType) reservoirType).getReservoirContent() == TweakedPumpjackHandler.ReservoirContent.GAS).
                collect(Collectors.toList());
    }

}
