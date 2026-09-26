package pet.itpuppy.woofaddons.commands

import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.TextColor
import pet.itpuppy.woofaddons.utils.Comp
import pet.itpuppy.woofaddons.utils.Comp.getUsernameComponent
import pet.itpuppy.woofaddons.utils.Comp.withBold

object CoordsCommand : ServerCommand {
    override fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                Commands.literal("coords").executes(::onExecuteCommand)
            )
        }
    }

    override fun onExecuteCommand(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source.player ?: return 0
        val broadcaster = ctx.source.server.playerList
        val currentDimension = player.level().dimension().identifier().toShortString()

        val positionComponent = Comp.of(
            "[${player.blockX} ${player.blockY} ${player.blockZ}]"
        ).withColor(TextColor.YELLOW)

        val dimensionComponent = Comp.of(
            "[${currentDimension.parseIdentifier()}]"
        ).withColor(TextColor.YELLOW)

        val message = Comp.build(
            Comp.of("COORD ").withBold(true),
            player.getUsernameComponent(),
            Comp.of(" is currently at "),
            positionComponent,
            Comp.of(" in "),
            dimensionComponent
        ).withColor(TextColor.GRAY)

        broadcaster.broadcastSystemMessage(message, false)
        return 1
    }

    private fun String.parseIdentifier() = this.split("_").joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
}