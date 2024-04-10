package io.github.srdjanv.tweakedpetroleumgas.client.hei;

import com.google.common.collect.Lists;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.common.Config;
import io.github.srdjanv.tweakedpetroleum.common.Configs;
import io.github.srdjanv.tweakedpetroleumgas.api.mixins.IGasReservoirType;
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
import java.util.function.Consumer;

import static flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler.reservoirList;

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
        Consumer<List<String>> warnings = list -> {
            if (reservoir.getDrainChance() != 1f) {
                list.add(HEIPumpjackUtil.translateToLocal("tweakedpetroleum.jei.reservoir.draw_chance1") + " " + reservoir.getDrainChance() * 100 +
                        HEIPumpjackUtil.translateToLocal("tweakedpetroleum.jei.reservoir.draw_chance2") + " " + (100f - (reservoir.getDrainChance() * 100)) +
                        HEIPumpjackUtil.translateToLocal("tweakedpetroleum.jei.reservoir.draw_chance3"));
            }

            if (Config.IPConfig.Extraction.req_pipes)
                list.add(HEIPumpjackUtil.translateToLocal("tweakedpetroleum.jei.reservoir.req_pipes"));

            list.add(HEIPumpjackUtil.translateToLocal("tweakedpetroleumgas.jei.reservoir.gas_info"));
        };

        return HEIPumpjackUtil.tooltipStrings(mouseX, mouseY, warnings, reservoir,
                this::getAverage, this::getStringWidth);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (Configs.TPConfig.HEIConfig.drawPowerTier) HEIPumpjackUtil.drawPowerTier(minecraft,57, 50, reservoir.getPowerTier());
        if (Configs.TPConfig.HEIConfig.drawSpawnWeight) HEIPumpjackUtil.drawSpawnWeight(minecraft,57, 70, reservoirList.get((PumpjackHandler.ReservoirType) reservoir));

        int warningCount = 1;

        if (Config.IPConfig.Extraction.req_pipes) {
            warningCount++;
        }

        if (reservoir.getDrainChance() != 1f) {
            warningCount++;
        }

        HEIPumpjackUtil.getPumpjackWarning().draw(minecraft, 56, 24);
        minecraft.fontRenderer.drawString(String.valueOf(warningCount), 56, 22, 16696077);

        if (getStringWidth() >= 77) {
            minecraft.fontRenderer.drawString(minecraft.fontRenderer.trimStringToWidth(
                    HEIPumpjackUtil.formatString(reservoir.getName()), 68).concat("..."), 6, 6, 15658734);
            return;
        }
        minecraft.fontRenderer.drawString(HEIPumpjackUtil.formatString(reservoir.getName()), 6, 6, 15658734);
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, GasStack ingredient, List<String> tooltip) {
        HEIPumpjackUtil.onTooltip(slotIndex, reservoir, ingredient.amount, ingredient.getGas().getLocalizedName(), tooltip);
    }

}
