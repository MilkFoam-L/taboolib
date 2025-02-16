dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":common-util"))
    compileOnly(project(":module:bukkit-nms"))
    // 服务端
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v12104:12104:mapped")
}

kotlin {
    jvmToolchain(21) // 仅针对该模块
}