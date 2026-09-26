package pet.itpuppy.woofaddons.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import pet.itpuppy.woofaddons.utils.Comp
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.argument
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import net.minecraft.world.item.component.ItemLore
import pet.itpuppy.woofaddons.utils.Comp.withBold

object LoreCommand : ServerCommand {
    private val color: TextColor = TextColor.fromRgb(0x8a8a8a)

    override fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                Commands.literal("lore").then(argument("lore", StringArgumentType.greedyString())
                    .executes(::onExecuteCommand))
            )
        }
    }

    override fun onExecuteCommand(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source.player ?: return 0
        val heldItem = player.activeItem
        val lore = StringArgumentType.getString(ctx, "lore")

        heldItem.set(DataComponents.LORE, ItemLore(
            lore.split("\\n").map { Comp.of(it).withColor(color) }
        ))

        val message = Comp.build(
            Comp.privateIconComponent,
            Comp.of(" LORE ").withBold(true),
            Comp.of("Applied lore to item "),
            heldItem.displayName
        ).withColor(TextColor.GRAY)

        player.sendSystemMessage(message)
        return 1
    }
}