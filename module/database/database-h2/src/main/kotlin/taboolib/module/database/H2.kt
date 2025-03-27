package taboolib.module.database

import taboolib.common.Inject
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency

@Inject
@RuntimeDependencies(
    RuntimeDependency(
        "!com.h2database:h2:2.1.214",
        test = "!org.h2.Driver",
        transitive = false
    ),
)
object H2