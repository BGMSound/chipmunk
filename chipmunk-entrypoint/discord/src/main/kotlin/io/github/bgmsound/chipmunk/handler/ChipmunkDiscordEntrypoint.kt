package io.github.bgmsound.chipmunk.handler

import io.github.bgmsound.chipmunk.ChipmunkEntrypoint
import net.dv8tion.jda.api.JDA
import org.springframework.stereotype.Component

@Component
class ChipmunkDiscordEntrypoint(
    private val jda: JDA
) : ChipmunkEntrypoint {
    override fun awaitShutdown() {
        jda.awaitShutdown()
    }
}