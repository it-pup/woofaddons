package pet.itpuppy.woofaddons.commands

import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.TextColor
import pet.itpuppy.woofaddons.utils.Comp
import pet.itpuppy.woofaddons.utils.Comp.getUsernameComponent
import pet.itpuppy.woofaddons.utils.Comp.withBold

object PleadCommand : ServerCommand {
    override fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                Commands.literal("plead").executes(::onExecuteCommand)
            )
        }
    }

    override fun onExecuteCommand(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source.player ?: return 0
        val broadcaster = ctx.source.server.playerList

        val message = Comp.build(
            Comp.of("PLEAD ").withBold(true),
            player.getUsernameComponent(),
            Comp.of(" pleads! \uD83E\uDD7A")
        ).withColor(TextColor.fromRgb(0xffe8a3))

        broadcaster.broadcastSystemMessage(message, false)
        return 1
    }
}