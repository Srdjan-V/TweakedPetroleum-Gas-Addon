package io.github.srdjanv.tweakedpetroleumgas.client.hei;

import com.google.common.collect.Lists;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.common.Config;
import io.github.srdjanv.tweakedpetroleumgas.api.util.IGasReservoirType;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.client.jei.MekanismJEI;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import io.github.srdjanv.tweakedlib.api.hei.BaseHEIUtil;
import io.github.srdjanv.tweakedpetroleum.util.HEIPumpjackUtil;

import java.util.List;

@SuppressWarnings("NullableProblems")
public class GasPumpjackWrapper implements IRecipeWrapper, ITooltipCallback<GasStack> {
    private final IGasReservoirType reservoir;
    private final Gas reservoirGas;

    public GasPumpjackWrapper(PumpjackHandler.ReservoirType reservoir) {
        this.reservoir = (IGasReservoirType) reservoir;
        reservoirGas = this.reservoir.getGas();
    }

    public GasStack getReplenishRateGas() {
        return new GasStack(reservoirGas, reservoir.getReplenishRate());
    }

    public int getPumpSpeed() {
        return reservoir.getPumpSpeed();
    }

    public int getMaxFluid() {
        return reservoir.getMaxSize();
    }

    public GasStack getAverageGas() {
        return new GasStack(reservoirGas, getAverage());
    }

    public int getAverage() {
        return (int) (((long) reservoir.getMaxSize() + (long) reservoir.getMinSize()) / 2);
    }

    private int getStringWidth() {
        return Math.min(77, Minecraft.getMinecraft().fontRenderer.getStringWidth(reservoir.getName()) + 6);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutputs(MekanismJEI.TYPE_GAS, Lists.newArrayList(getAverageGas()));
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        String[][] strings = new String[3][];

        if (reservoir.getDrainChance() != 1f) {
            strings[0] = new String[]{"tweakedpetroleum.jei.reservoir.draw_chance",
                    String.valueOf(reservoir.getDrainChance() * 100),
                    String.valueOf(100f - (reservoir.getDrainChance() * 100))};
        }

        if (Config.IPConfig.Extraction.req_pipes) {
            strings[2] = new String[]{"tweakedpetroleum.jei.reservoir.req_pipes"};
        }

        strings[1] = new String[]{"tweakedpetroleumgas.jei.reservoir.gas_info"};

        return HEIPumpjackUtil.tooltipStrings(mouseX, mouseY, strings, reservoir,
                this::getAverage, this::getStringWidth);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int warningCount = 1;

        if (Config.IPConfig.Extraction.req_pipes) {
            warningCount++;
        }

        if (reservoir.getDrainChance() != 1f) {
            warningCount++;
        }

        BaseHEIUtil.getPumpjackWarning().draw(minecraft, 55, 8);
        minecraft.fontRenderer.drawString(String.valueOf(warningCount), 55, 6, 16696077);
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, GasStack ingredient, List<String> tooltip) {
        HEIPumpjackUtil.onTooltip(slotIndex, reservoir, ingredient.amount, ingredient.getGas().getLocalizedName(), tooltip);
    }

}
