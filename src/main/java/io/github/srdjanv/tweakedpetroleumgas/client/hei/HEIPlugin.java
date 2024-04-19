package io.github.srdjanv.tweakedpetroleumgas.client.hei;

import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import io.github.srdjanv.tweakedpetroleum.api.mixins.ITweakedPetReservoirType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil;
import io.github.srdjanv.tweakedpetroleum.api.crafting.TweakedPumpjackHandler;

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
        registry.addRecipeCatalyst(BaseHEIUtil.getPumpjackCatalyst(), GasPumpjackCategory.UID);
    }

    private Collection<PumpjackHandler.ReservoirType> getRecipes() {
        return PumpjackHandler.reservoirList.keySet().stream().
                filter(reservoirType -> ((ITweakedPetReservoirType) reservoirType).getReservoirContent() == TweakedPumpjackHandler.ReservoirContent.GAS).
                collect(Collectors.toList());
    }
}
