package pet.itpuppy.woofaddons.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.network.chat.TextColor
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import pet.itpuppy.woofaddons.utils.Comp
import pet.itpuppy.woofaddons.utils.Comp.getUsernameComponent
import pet.itpuppy.woofaddons.utils.Comp.withBold

object ShowCommand : ServerCommand {
    private val shows = listOf("SHOW", "LOOK", "HERE")

    override fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            dispatcher.register(
                Commands.literal("show")
                    .executes(::onExecuteCommand)
                    .then(
                        Commands.argument("type", StringArgumentType.word())
                            .suggests { _, builder ->
                                SharedSuggestionProvider.suggest(
                                    ItemType.entries.map { type -> type.name.lowercase() },
                                    builder
                                )
                            }
                            .executes(::onExecuteCommand)
                    )
            )
        }
    }

    override fun onExecuteCommand(ctx: CommandContext<CommandSourceStack>): Int {
        val type = runCatching {
            val input = StringArgumentType.getString(ctx, "type")
            ItemType.entries.find { enum -> enum.name.equals(input, true) }
        }.getOrNull() ?: ItemType.HELD

        val player = ctx.source.player ?: return 0
        val itemStack = type.getter(player)
        val broadcaster = ctx.source.server.playerList

        val message = Comp.build(
            Comp.of("${shows.random()} ").withBold(true),
            player.getUsernameComponent(),
            Comp.of(" is ${type.holdingDescription} "),
            itemStack.displayName
        ).withColor(TextColor.GRAY)

        if (itemStack.count > 1) message.append(" (x${itemStack.count})")

        broadcaster.broadcastSystemMessage(message, false)
        return 1
    }

    enum class ItemType(val holdingDescription: String, val getter: (ServerPlayer) -> ItemStack) {
        HELD("holding", { it.mainHandItem }),
        OFFHAND("holding", { it.offhandItem }),
        HELMET("wearing", { it.getItemBySlot(EquipmentSlot.HEAD) }),
        CHESTPLATE("wearing", { it.getItemBySlot(EquipmentSlot.CHEST) }),
        LEGGINGS("wearing", { it.getItemBySlot(EquipmentSlot.LEGS) }),
        BOOTS("wearing", { it.getItemBySlot(EquipmentSlot.FEET) })
    }
}