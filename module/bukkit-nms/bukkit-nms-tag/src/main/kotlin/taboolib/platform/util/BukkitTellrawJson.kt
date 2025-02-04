package taboolib.platform.util

import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.tabooproject.reflex.Reflex.Companion.getProperty
import org.tabooproject.reflex.Reflex.Companion.invokeMethod
import taboolib.common.util.unsafeLazy
import taboolib.module.chat.ComponentText
import taboolib.module.chat.RawMessage
import taboolib.module.nms.NMSItemTag

fun ItemStack.toNMSKeyAndItemData(): Pair<String, String> {
    val nmsItemStack = NMSItemTag.instance.getNMSCopy(this)
    val nmsKey = try {
        "${type.key.namespace}:${type.key.key}"
    } catch (ex: NoSuchMethodError) {
        // #359
        // 错误的获取方式
        // val nmsItem = nmsItemStack.invokeMethod<Any>("getItem")!!
        // val name = nmsItem.getProperty<String>("name")!!
        // var key = ""
        // name.forEach { c ->
        //     if (c.isUpperCase()) {
        //         key += "_" + c.lowercase()
        //     } else {
        //         key += c
        //     }
        // }
        // key
        val nmsItem = nmsItemStack.invokeMethod<Any>("getItem")!!
        val nmsKey = classNMSItem.getProperty<Any>("REGISTRY", isStatic = true)!!.invokeMethod<Any>("b", nmsItem)!!
        nmsKey.invokeMethod<String>("getKey")!!
    }
    return nmsKey to NMSItemTag.instance.toMinecraftJson(this)
}

fun ComponentText.hoverItem(itemStack: ItemStack): ComponentText {
    val (key, data) = itemStack.toNMSKeyAndItemData()
    return hoverItem(key, data)
}

fun RawMessage.hoverItem(itemStack: ItemStack): RawMessage {
    val (key, data) = itemStack.toNMSKeyAndItemData()
    return hoverItem(key, data)
}

private val classNMSItem by unsafeLazy {
    nmsClassLegacy("Item")
}

private fun nmsClassLegacy(name: String): Class<*> {
    return Class.forName("net.minecraft.server.${Bukkit.getServer().javaClass.name.split('.')[3]}.$name")
}