package pet.itpuppy.woofaddons.commands

import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.TextColor
import pet.itpuppy.woofaddons.utils.Comp
import pet.itpuppy.woofaddons.utils.Comp.getUsernameComponent
import pet.itpuppy.woofaddons.utils.Comp.withBold

object BoopCommand : ServerCommand {
    override fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                Commands.literal("boop")
                    .then(
                        argument("player", EntityArgument.player()).executes(::onExecuteCommand)
                    )
            )
        }
    }

    override fun onExecuteCommand(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source.player ?: return 0
        val target = EntityArgument.getPlayer(ctx, "player")

        val playerMessage = Comp.build(
            Comp.privateIconComponent,
            Comp.of(" BOOP ").withBold(true),
            Comp.of("You booped "),
            target.getUsernameComponent(),
            Comp.of("!")
        ).withColor(TextColor.LIGHT_PURPLE)

        val targetMessage = Comp.build(
            Comp.privateIconComponent,
            Comp.of(" BOOP ").withBold(true),
            player.getUsernameComponent(),
            Comp.of(" booped you!")
        ).withColor(TextColor.LIGHT_PURPLE)

        player.sendSystemMessage(playerMessage)
        target.sendSystemMessage(targetMessage)

        return 1
    }
}
