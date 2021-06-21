package taboolib.module.chat

import net.md_5.bungee.api.ChatColor

fun String.colored() = HexColor.translate(this)

fun String.uncolored() = ChatColor.stripColor(this)!!

fun List<String>.colored() = map { it.colored() }

fun List<String>.uncolored() = map { it.uncolored() }