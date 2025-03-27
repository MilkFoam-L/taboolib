package taboolib.platform

import net.afyer.afybroker.server.Broker
import net.afyer.afybroker.server.plugin.Command
import net.afyer.afybroker.server.plugin.TabExecutor
import org.tabooproject.reflex.Reflex.Companion.getProperty
import taboolib.common.Inject
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandCompleter
import taboolib.common.platform.command.CommandExecutor
import taboolib.common.platform.command.CommandStructure
import taboolib.common.platform.command.component.CommandBase
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.warning
import taboolib.common.platform.service.PlatformCommand
import taboolib.common.util.t
import taboolib.platform.type.AfyBrokerCommandSender
import taboolib.platform.util.afyBrokerPlugin

/**
 * TabooLib
 * taboolib.platform.AfyBrokerCommand
 *
 * @author Ling556
 * @since 2024/5/09 23:51
 */
@Awake
@Inject
@PlatformSide(Platform.AFYBROKER)
class AfyBrokerCommand : PlatformCommand {

    override fun registerCommand(
        command: CommandStructure,
        executor: CommandExecutor,
        completer: CommandCompleter,
        commandBuilder: CommandBase.() -> Unit,
    ) {
        Broker.getPluginManager().registerCommand(AfyBrokerPlugin.getInstance(), object : Command(command.name,*command.aliases.toTypedArray()), TabExecutor {
            override fun execute(args: Array<String>) {
                executor.execute(adaptCommandSender(AfyBrokerCommandSender), command, command.name, args)
            }

            override fun onTabComplete(args: Array<String>): MutableIterable<String> {
                return completer.execute(adaptCommandSender(AfyBrokerCommandSender), command, command.name, args)?.toMutableList() ?: ArrayList()
            }
        })
    }

    override fun unregisterCommand(command: String) {
        val instance = Broker.getPluginManager().getProperty<MutableMap<String, Command>>("commandMap")!![command] ?: return
        Broker.getPluginManager().unregisterCommand(instance)
    }

    override fun unregisterCommands() {
        Broker.getPluginManager().unregisterCommands(AfyBrokerPlugin.getInstance())
    }

    override fun unknownCommand(sender: ProxyCommandSender, command: String, state: Int) {
        when (state) {
            1 -> warning(
                """
                    命令未找到。
                    Command not found.
                """.t()
            )
            2 -> warning(
                """
                    未知或不完整的命令。
                    Unknown or incomplete command.
                """.t()
            )
            else -> return
        }
    }
}