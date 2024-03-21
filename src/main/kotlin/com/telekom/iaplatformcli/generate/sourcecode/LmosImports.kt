package com.telekom.iaplatformcli.generate.sourcecode

import org.springframework.stereotype.Component

interface LmosImports {
    fun getLmosImports(): List<String>
    fun getAgentConstants(): List<String>
    fun getLmosImportsForController(): List<String>
    fun getLmosImportForStep(): List<String>

    fun getAsString(imports: List<String>): String {
        return imports.joinToString(separator = "\n")
    }
}

@Component
class KotlinLmosImports : LmosImports {

    companion object {
        private const val LMOS_BOOT_PROPERTIES = "import com.telekom.lmos.boot.KernelProperties"
        private const val LMOS_KERNEL_TENANT = "import com.telekom.lmos.kernel.tenant.TenantProvider\n" +
            "import com.telekom.lmos.kernel.model.Tenant"
        private const val LMOS_KERNEL_STEPS = "import com.telekom.lmos.kernel.steps.*\n" +
            "import com.telekom.lmos.kernel.UnknownTenantException"
        private const val LMOS_KERNEL_MODEL = "import com.telekom.lmos.kernel.model.*\n" +
            "import com.telekom.lmos.kernel.getOrNull\n"
        const val LMOS_KERNEL_AGENT = "import com.telekom.lmos.kernel.agent.Agent\n" +
            "import com.telekom.lmos.kernel.agent.AgentProfile"
        const val LMOS_KERNEL_USER = "import com.telekom.lmos.kernel.user.UserInformation\n" +
            "import com.telekom.lmos.kernel.user.UserProvider\n"
        const val LMOS_KERNEL_REQUEST = "import com.telekom.lmos.kernel.steps.Input\n" +
            "import com.telekom.lmos.kernel.steps.RequestContext\n" +
            "import com.telekom.lmos.kernel.steps.RequestStatus"
        const val SPRING_BOOT_COMPONENT_IMPORTS = "import org.springframework.stereotype.Component\n"
        const val SPRING_BOOT_CONTROLLER_IMPORTS =
            "import org.springframework.http.ResponseEntity\n" +
                "import org.springframework.web.bind.annotation.*"

        const val LOGGER_IMPORTS = "import org.slf4j.Logger\n" +
            "import org.slf4j.LoggerFactory"
        const val AGENT_CONSTANTS_NATCO_CODE = "NATCO_CODE = \"natco_code\""
        const val AGENT_CONSTANTS_USER = "USER = \"user\""
    }

    override fun getLmosImports(): List<String> {
        return listOf(
            LMOS_BOOT_PROPERTIES,
            // PLATFORM_IMPORTS,
            LMOS_KERNEL_TENANT,
            LMOS_KERNEL_STEPS,
            LMOS_KERNEL_USER,
            LMOS_KERNEL_AGENT,
            SPRING_BOOT_COMPONENT_IMPORTS,
            LOGGER_IMPORTS,
        )
    }

    override fun getAgentConstants(): List<String> {
        return listOf(AGENT_CONSTANTS_NATCO_CODE, AGENT_CONSTANTS_USER)
    }

    override fun getLmosImportsForController(): List<String> {
        return listOf(LMOS_KERNEL_REQUEST, LMOS_KERNEL_USER, LOGGER_IMPORTS, SPRING_BOOT_CONTROLLER_IMPORTS)
    }

    override fun getLmosImportForStep(): List<String> {
        return listOf(LMOS_KERNEL_MODEL, LMOS_KERNEL_STEPS, SPRING_BOOT_COMPONENT_IMPORTS)
    }
}
