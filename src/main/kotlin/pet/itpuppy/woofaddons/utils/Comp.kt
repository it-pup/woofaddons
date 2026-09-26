package pet.itpuppy.woofaddons.utils

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.TextColor
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import kotlin.math.floor
import kotlin.math.pow

object Comp {
    fun build(vararg components: Component): MutableComponent {
        val final = Component.empty()

        for (component in components) {
            final.append(component)
        }

        return final
    }

    fun of(text: String): MutableComponent = Component.literal(text)

    fun MutableComponent.withBold(bold: Boolean): Component = this.withStyle { s -> s.withBold(bold) }
    fun MutableComponent.withHoverText(hoverComponent: Component): Component {
        return this.withStyle { s -> s.withHoverEvent(HoverEvent.ShowText(hoverComponent)) }
    }

    fun ServerPlayer.getUsernameComponent(): Component {
        val playtime = this.stats.getValue(Stats.CUSTOM.get(Stats.PLAY_TIME)) / 20.0 / 3600.0
        val deathcount = this.stats.getValue(Stats.CUSTOM.get(Stats.DEATHS))
        val expLvl = this.experienceLevel

        val hoverComponent = build(
            of("\uD83D\uDC64 "),
            this.displayName.copy().withColor(this.teamColor),
            newline,

            of(this.stringUUID),
            newline, newline,

            of("⌚ Playtime: "),
            of("${playtime.floorTo(1)}h").withColor(TextColor.GRAY),
            newline,

            of("☠ Deaths: "),
            of("$deathcount").withColor(TextColor.GRAY),
            newline,

            of("⭐ EXP Level: "),
            of("$expLvl").withColor(TextColor.GRAY)
        ).withColor(TextColor.DARK_GRAY)

        return this.displayName.copy().withColor(this.teamColor).withHoverText(hoverComponent)
    }

    fun Double.floorTo(decimals: Int): Double {
        val multiplier = 10.0.pow(decimals)
        return floor(this * multiplier) / multiplier
    }

    val privateIconComponent: Component = of("§8[§7!§8]§r").withHoverText(of("§7Only you can see this message.§r"))
    val newline: Component = of("\n")
}