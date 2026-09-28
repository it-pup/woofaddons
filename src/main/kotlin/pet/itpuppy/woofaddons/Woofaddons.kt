package pet.itpuppy.woofaddons

import net.fabricmc.api.EnvType
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pet.itpuppy.woofaddons.commands.ServerCommand
import pet.itpuppy.woofaddons.events.ServerEvent

object Woofaddons : ModInitializer {
	const val MOD_ID: String = "woofaddons"
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		LOGGER.info("woof!")

        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
			LOGGER.info("running as client!")

			ServerEvent::class.sealedSubclasses
				.mapNotNull { it.objectInstance }
				.forEach { it.register() }

			ServerCommand::class.sealedSubclasses
				.mapNotNull { it.objectInstance }
				.forEach { it.register() }
		} else {
			LOGGER.info("running as server!")
		}
	}

	fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
