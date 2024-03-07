package com.telekom.iaplatformcli.generate.sourcecode

import com.telekom.iaplatformcli.service.writer.IndentedWriter
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

@Component
class KotlinSourceCode(val lmosImports: LmosImports) : SourceCode {

    override fun createAgentCode(packageName: String, agentFile: File, agentName: String, steps: List<String>) {
        val writer = IndentedWriter(agentFile)
        //in case of existing file, clear the file first
        agentFile.writeText("")
        val packageDeclaration = "package ${packageName}"
        val consolidatedImports = StringBuilder()
        lmosImports.getLmosImports().forEach { import -> consolidatedImports.append("\n$import") }

        //consolidate all steps into a string
        val consolidatedSteps = StringBuilder()
        steps.forEach { step -> consolidatedSteps.append("\n.step<$step>") }

        val classBody = """
const val NATCO_CODE = "natco_code"
const val USER = "user"
            
            
@Component
class ${agentName} (
    private val stepExecutor: StepExecutor,
    private val tenantProvider: TenantProvider,
    private val userProvider: UserProvider,
    private val kernelProperties: KernelProperties
) : Agent() {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun profile() = AgentProfile(name = "SupervisorAssistant", purpose = "", dp = "") // TODO

    override suspend fun executeInternal(input: Input): Output {
        val user = input.context<UserInformation?>(USER, null)
        val natcoCode = input.context<String>(NATCO_CODE)
        val tenant = kernelProperties.getTenant(natcoCode) ?: throw UnknownTenantException(natcoCode)

        return userProvider.setUser(user ?: UserInformation()) {
            tenantProvider.setTenant(tenant) {
                val result = stepExecutor
                    .seq()
                    ${consolidatedSteps}
                    .end()
                    .execute(input)
                result
            }
        }
    }
}
        """.trimIndent()
        writer.writeLine(packageDeclaration);
        writer.writeLine(consolidatedImports.toString())
        writer.writeLine(classBody)
    }

}