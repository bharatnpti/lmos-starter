package com.telekom.iaplatformcli.generate.sourcecode

import org.springframework.stereotype.Component

interface LmosImports {
    fun getLmosImports(): List<String>
    fun getAgentConstants(): List<String>
}

@Component
class KotlinLmosImports : LmosImports {

    companion object {
        const val LMOS_BOOT_PROPERTIES = "import com.telekom.lmos.boot.KernelProperties"
        const val LMOS_KERNEL_TENANT = "import com.telekom.lmos.kernel.tenant.TenantProvider"
        const val LMOS_KERNEL_STEPS = "import com.telekom.lmos.kernel.steps.*" +
            "import com.telekom.lmos.kernel.UnknownTenantException"
        const val LMOS_KERNEL_AGENT = "import com.telekom.lmos.kernel.agent.Agent\n" +
            "import com.telekom.lmos.kernel.agent.AgentProfile"
        const val LMOS_KERNEL_USER = "import com.telekom.lmos.kernel.user.UserInformation\n" +
            "import com.telekom.lmos.kernel.user.UserProvider"
        const val SPRING_BOOT_IMPORTS = "import org.springframework.stereotype.Component"
        const val LOGGER_IMPORTS = "import org.slf4j.LoggerFactory"
        const val PLATFORM_IMPORTS = "import com.telekom.ia.platform.assistants.anonymization.steps.Anonymize\n" +
            "import com.telekom.ia.platform.assistants.anonymization.steps.Deanonymize\n" +
            "import com.telekom.ia.platform.assistants.billing.BillingAgent\n" +
            "import com.telekom.ia.platform.assistants.faq.FAQAgent\n" +
            "import com.telekom.ia.platform.assistants.faq.steps.*\n" +
            "import com.telekom.ia.platform.assistants.steps.HandleAuthResult\n" +
            "import com.telekom.ia.platform.assistants.steps.LegacySaveResponse\n" +
            "import com.telekom.ia.platform.assistants.steps.LogFinalResponse\n" +
            "import com.telekom.ia.platform.assistants.steps.SaveResponse\n" +
            "import com.telekom.ia.platform.assistants.steps.*\n" +
            "import com.telekom.ia.platform.inbound.DialogEvent\n" +
            "import com.telekom.ia.platform.inbound.withLogEventContext"

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
            SPRING_BOOT_IMPORTS,
            LOGGER_IMPORTS,
        )
    }

    override fun getAgentConstants(): List<String> {
        return listOf(AGENT_CONSTANTS_NATCO_CODE, AGENT_CONSTANTS_USER)
    }
}
