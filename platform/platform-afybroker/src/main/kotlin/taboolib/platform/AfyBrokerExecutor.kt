package taboolib.platform

import net.afyer.afybroker.server.Broker
import taboolib.common.Inject
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.service.PlatformExecutor
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

/**
 * TabooLib
 * taboolib.platform.AppExecutor
 *
 * @author CziSKY
 * @since 2021/6/16 0:43
 */
@Awake
@Inject
@PlatformSide(Platform.AFYBROKER)
class AfyBrokerExecutor : PlatformExecutor {

    private val tasks = ArrayList<PlatformExecutor.PlatformRunnable>()
    private var started = false

    @Awake(LifeCycle.ENABLE)
    override fun start() {
        started = true
        // 提交列队中的任务
        tasks.forEach { submit(it) }
    }

    override fun submit(runnable: PlatformExecutor.PlatformRunnable): PlatformExecutor.PlatformTask {
        return if (started) {
            val future = CompletableFuture<Unit>()
            val task = BrokerPlatformTask(future)
            val scheduledTask = when {
                runnable.now -> {
                    runnable.executor(task)
                    null
                }
                runnable.period > 0 -> {
                    Broker.getScheduler().schedule(AfyBrokerPlugin.getInstance(), { runnable.executor(task) }, runnable.delay * 50L, runnable.period * 50L, TimeUnit.MILLISECONDS)
                }
                runnable.delay > 0 -> {
                    Broker.getScheduler().schedule(AfyBrokerPlugin.getInstance(), { runnable.executor(task) }, runnable.delay * 50L, TimeUnit.MILLISECONDS)
                }
                else -> {
                    Broker.getScheduler().runAsync(AfyBrokerPlugin.getInstance()) { runnable.executor(task) }
                }
            }
            future.thenAccept {
                scheduledTask?.cancel()
            }
            task
        } else {
            tasks += runnable
            object : PlatformExecutor.PlatformTask {

                override fun cancel() {
                    tasks -= runnable
                }
            }
        }
    }

    class BrokerPlatformTask(private val future: CompletableFuture<Unit>) : PlatformExecutor.PlatformTask {

        override fun cancel() {
            future.complete(null)
        }
    }

}