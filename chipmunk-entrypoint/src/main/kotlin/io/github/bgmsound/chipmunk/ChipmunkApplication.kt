package io.github.bgmsound.chipmunk

import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
internal class ChipmunkApplication {
    fun run(args: Array<String>) {
        val applicationContext = SpringApplication(ChipmunkApplication::class.java).apply {
            webApplicationType = WebApplicationType.NONE
        }.run(*args)
        for (entrypoint in applicationContext.getBeansOfType(ChipmunkEntrypoint::class.java).values) {
            entrypoint.awaitShutdown()
        }
    }
}

fun main(args: Array<String>) {
    ChipmunkApplication().run(args)
}