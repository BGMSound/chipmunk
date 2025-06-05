package io.github.bgmsound.chipmunk.handler

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.EnumSet

@Configuration
class JDAEntrypointConfiguration(
    @Value ("\${app.entrypoint.discord.token}") private val token: String,
    private val listeners: List<ListenerAdapter>
) {
    @Bean
    fun jda(): JDA {
        return JDABuilder
            .createDefault(
                token,
                EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGE_REACTIONS)
            )
            .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
            .addEventListeners(*listeners.toTypedArray())
            .build()
    }
}