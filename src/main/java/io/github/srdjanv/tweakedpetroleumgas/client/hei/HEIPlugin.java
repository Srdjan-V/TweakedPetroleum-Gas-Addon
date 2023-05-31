package io.github.srdjanv.tweakedpetroleumgas.client.hei;

import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil;
import io.github.srdjanv.tweakedpetroleum.api.crafting.TweakedPumpjackHandler;
import io.github.srdjanv.tweakedpetroleum.api.mixins.IReservoirType;

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
        registry.addRecipes(PumpjackHandler.reservoirList.keySet().stream().
                filter(reservoirType -> ((IReservoirType)reservoirType).getReservoirContent() == TweakedPumpjackHandler.ReservoirContent.GAS).
                collect(Collectors.toList()), GasPumpjackCategory.UID);
        registry.addRecipeCatalyst(BaseHEIUtil.getPumpjackCatalyst(), GasPumpjackCategory.UID);
    }

}
