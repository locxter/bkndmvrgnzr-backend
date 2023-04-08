package com.github.locxter.bkndmvrgnzr.backend

import com.github.locxter.bkndmvrgnzr.backend.lib.DataLoader
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Main(private val dataLoader: DataLoader) {
    @Bean
    fun handleArguments(ctx: ApplicationContext): CommandLineRunner {
        return CommandLineRunner { args: Array<String> ->
            if (args.contains("--initialize") || args.contains("-i")) {
                dataLoader.loadData()
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}