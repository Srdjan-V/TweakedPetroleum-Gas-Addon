package srki2k.tweakedpetroleumgas.client.hei;

import com.google.common.collect.Lists;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import flaxbeard.immersivepetroleum.common.Config;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import mekanism.client.jei.MekanismJEI;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import srki2k.tweakedpetroleum.util.HEIUtil;
import srki2k.tweakedpetroleumgas.api.util.IGasReservoirType;

import java.util.List;

@SuppressWarnings("NullableProblems")
public class GasPumpjackWrapper implements IRecipeWrapper {
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
        return new GasStack(reservoirGas, (int) (((long) reservoir.getMaxSize() + (long) reservoir.getMinSize()) / 2));
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setOutputs(MekanismJEI.TYPE_GAS, Lists.newArrayList(getAverageGas()));
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        String[][] strings = new String[3][];

        if (reservoir.getDrainChance() != 1f) {
            strings[0] = new String[]{"jei.pumpjack.reservoir.draw_chance",
                    String.valueOf(reservoir.getDrainChance() * 100),
                    String.valueOf(100f - (reservoir.getDrainChance() * 100))};
        }

        if (Config.IPConfig.Extraction.req_pipes) {
            strings[2] = new String[]{"jei.pumpjack.reservoir.req_pipes"};
        }

        strings[1] = new String[]{"jei.pumpjack.reservoir.gas_info"};

        return HEIUtil.tooltipStrings(mouseX, mouseY, strings, reservoir);
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

        HEIUtil.getPumpjackWarning().draw(minecraft, 58, 8);
        minecraft.fontRenderer.drawString(String.valueOf(warningCount), 58, 8, 16696077);
    }

}
