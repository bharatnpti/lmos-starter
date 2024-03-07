package com.telekom.iaplatformcli.generate.sourcecode

import org.springframework.stereotype.Component

interface LmosImports {
    fun getLmosImports(): List<String>
}
@Component
class KotlinLmosImports : LmosImports{

    companion object{
        const val LMOS_BOOT_PROPERTIES = "import com.telekom.lmos.boot.KernelProperties"
        const val LMOS_KERNEL_TENANT="import com.telekom.lmos.kernel.tenant.TenantProvider"
        const val LMOS_KERNEL_STEPS = "import com.telekom.lmos.kernel.steps.*"
        const val LMOS_KERNEL_AGENT="import com.telekom.lmos.kernel.agent.Agent\n" +
                "import com.telekom.lmos.kernel.agent.AgentProfile"
        const val LMOS_KERNEL_USER="import com.telekom.lmos.kernel.user.UserInformation\n" +
                "import com.telekom.lmos.kernel.user.UserProvider"
        const val SPRING_BOOT_IMPORTS = "import org.springframework.stereotype.Component"
    }

    override fun getLmosImports(): List<String> {
        return listOf(LMOS_BOOT_PROPERTIES, LMOS_KERNEL_TENANT, LMOS_KERNEL_STEPS, LMOS_KERNEL_USER, LMOS_KERNEL_AGENT, SPRING_BOOT_IMPORTS);
    }
}