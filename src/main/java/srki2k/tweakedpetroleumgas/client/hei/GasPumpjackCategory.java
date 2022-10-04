package srki2k.tweakedpetroleumgas.client.hei;

import mekanism.api.gas.GasStack;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.gas.GasStackRenderer;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import srki2k.tweakedlib.api.hei.BaseHEIUtil;
import srki2k.tweakedpetroleumgas.TweakedPetroleumGas;

@SuppressWarnings("NullableProblems")
public class GasPumpjackCategory implements IRecipeCategory<GasPumpjackWrapper> {
    public static final String UID = TweakedPetroleumGas.MODID + ".pumpjack";

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return BaseHEIUtil.translateToLocal("gui.gas") + " " + BaseHEIUtil.translateToLocal("tile.immersivepetroleum.metal_multiblock.pumpjack.name");
    }

    @Override
    public String getModName() {
        return TweakedPetroleumGas.MODID;
    }

    @Override
    public IDrawable getBackground() {
        return BaseHEIUtil.getPumpjackBackground();
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, GasPumpjackWrapper recipeWrapper, IIngredients ingredients) {
        IGuiIngredientGroup<GasStack> gasStacks = recipeLayout.getIngredientsGroup(MekanismJEI.TYPE_GAS);
        gasStacks.addTooltipCallback(recipeWrapper);

        GasStackRenderer renderer1 = new GasStackRenderer(recipeWrapper.getMaxFluid(), false, 16, 60, null);
        gasStacks.init(0, false, renderer1, 12, 10, 16, 60, 0, 0);
        gasStacks.set(0, recipeWrapper.getAverageGas());

        GasStackRenderer renderer2 = new GasStackRenderer(recipeWrapper.getPumpSpeed(), false, 16, 60, null);
        gasStacks.init(1, false, renderer2, 36, 10, 16, 60, 0, 0);
        gasStacks.set(1, recipeWrapper.getReplenishRateGas());

    }

}

