//Register an power tier
var power = mods.tweakedMods.PowerTier.recipeBuilder()
        .capacity(5555555)
        .rft(5)
        .register()

/*
This method will register a reservoir with the chance to drain fluid from the chunk.
If drainChance is set to 0.25 it will have a 25% chance to drain from the chunk
If drainChance is set to 0.5 it will have a 50% chance to drain from the chunk
If drainChance is set to 1 it will have a 100% chance to drain from the chunk, its the same as registering it normally
*/

//Register an gas reservoir
mods.tweakedMods.GasReservoir.recipeBuilder()
        .name("GroovyGasReservoir")
        .fluid(gas("hydrogen"))
        .weight(80000)
        .powerTier(power)
        .minSize(50)
        .maxSize(1500)
        .pumpSpeed(10)
        .replenishRate(5)
        .register()

//Remove an gas reservoir
mods.tweakedMods.GasReservoir.remove("Some_Name")

//Modify an reservoir
mods.tweakedMods.GasReservoir.get("lava").toBuilder()
        .drainChance(0.8).maxSize(Integer.MAX_VALUE).register()

/*
Groovy compat is located in 'io.github.srdjanv.tweakedpetroleum.common.compat.groovy'

Also see https://cleanroommc.com/groovy-script/
*/