package pet.itpuppy.woofaddons.commands

import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.TextColor
import pet.itpuppy.woofaddons.utils.Comp
import pet.itpuppy.woofaddons.utils.Comp.withBold

object ResetLoreCommand : ServerCommand {
    override fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                Commands.literal("resetlore").executes(::onExecuteCommand)
            )
        }
    }

    override fun onExecuteCommand(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source.player ?: return 0
        val heldItem = player.activeItem

        val originalItem = BuiltInRegistries.ITEM.getValue(
            BuiltInRegistries.ITEM.getKey(heldItem.item)
        )

        heldItem.set(
            DataComponents.LORE,
            originalItem.components().get(DataComponents.LORE)
        )

        val message = Comp.build(
            Comp.privateIconComponent,
            Comp.of(" LORE ").withBold(true),
            Comp.of("Reset lore for item "),
            heldItem.displayName
        ).withColor(TextColor.GRAY)

        player.sendSystemMessage(message)
        return 1
    }
}