package pet.itpuppy.woofaddons.commands

import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.TextColor
import pet.itpuppy.woofaddons.utils.Comp
import pet.itpuppy.woofaddons.utils.Comp.getUsernameComponent
import pet.itpuppy.woofaddons.utils.Comp.withBold

object BarkCommand : ServerCommand {
    private val barks = listOf("BARK", "WOOF", "AWRF", "AWOO", "ARFF")

    override fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                Commands.literal("bark").executes(::onExecuteCommand)
            )
        }
    }

    override fun onExecuteCommand(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source.player ?: return 0
        val broadcaster = ctx.source.server.playerList

        val message = Comp.build(
            Comp.of("${barks.random()} ").withBold(true),
            player.getUsernameComponent(),
            Comp.of(" barks!")
        ).withColor(TextColor.fromRgb(0xffc387))

        broadcaster.broadcastSystemMessage(message, false)
        return 1
    }
}