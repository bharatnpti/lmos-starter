package com.telekom.iaplatformcli.generate.sourcecode

import com.telekom.iaplatformcli.service.writer.IndentedWriter
import org.springframework.stereotype.Component
import java.io.File

@Component
class KotlinSourceCode(val lmosImports: LmosImports) : SourceCode {

    override fun createAgentCode(packageName: String, agentFile: File, agentName: String, steps: List<String>, customImport: MutableList<String>) {
        val writer = IndentedWriter(agentFile)
        // in case of existing file, clear the file first
        agentFile.writeText("")
        val packageDeclaration = "package $packageName\n"

        val imports = lmosImports.getLmosImports().toMutableList()
        imports.addAll(customImport)
        val consolidatedImports = lmosImports.getAsString(imports)

        // consolidate all steps into a string
        val consolidatedSteps = StringBuilder()
        steps.forEach { step ->
            consolidatedSteps.append(
                """
                    .step<$step>()""",
            )
        }

        val classBodyStart = """

@Component
class $agentName (
    private val stepExecutor: StepExecutor,
    private val tenantProvider: TenantProvider,
    private val userProvider: UserProvider,
    private val kernelProperties: KernelProperties
) : Agent() {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun profile() = AgentProfile(name = "$agentName", purpose = "", dp = "") // TODO

    override suspend fun executeInternal(input: Input): Output {
        val user = input.context<UserInformation?>(USER, null)
        val natcoCode = input.context<String>(NATCO_CODE)
        val defaultTenant = Tenant("de", "german", languageModel = "gpt-3.5-4k")

        return userProvider.setUser(user ?: UserInformation()) {
            tenantProvider.setTenant(defaultTenant) {
                val result = stepExecutor
                    .seq()
        """.trimIndent()

        val classBodyEnd = """
                    .end()
                    .execute(input)
                result
            }
        }
    }
}
        """.trimIndent()
        writer.writeLine(packageDeclaration)
        writer.writeLine(consolidatedImports)
        writer.writeLine(classBodyStart)
        writer.writeLine(consolidatedSteps.toString())
        writer.writeLine(classBodyEnd)
    }

    override fun createAgentConstantsCode(packageName: String, agentConstantsFile: File, agentConstantsClass: String) {
        val writer = IndentedWriter(agentConstantsFile)

        writer.writeLine("package $packageName\n")

        val agentConstants = this.lmosImports.getAgentConstants()
        val consolidatedConstants = StringBuilder()
        agentConstants.forEach { constant ->
            consolidatedConstants.append(
                "const val $constant\n",
            )
        }
        writer.writeLine(consolidatedConstants.toString())
    }

    override fun createAgentControllerCode(agentName: String, controllerFile: File, packageName: String, customImports: MutableList<String>) {
        val writer = IndentedWriter(controllerFile)

        controllerFile.writeText("")
        val packageDeclaration = "package $packageName\n"

        val imports = lmosImports.getLmosImportsForController().toMutableList()
        imports.addAll(customImports)
        val consolidatedImports = lmosImports.getAsString(imports)

        val agentRef = "${agentName.replaceFirstChar { it.lowercase() }}Assistant"
        val classBody = """
            @RestController
            @RequestMapping("/agent")
            @CrossOrigin(
                origins = ["*"],
                allowedHeaders = ["*"],
                exposedHeaders = ["*"],
                methods = [RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS]
            )
            class ${agentName}Controller (
                val $agentRef : $agentName
            ) {

                val log: Logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)

                @PostMapping("/tenants/{tenantId}/conversations/{conversationId}/turns/{turnId}")
                suspend fun createTurn(
                    @RequestBody turnMsg: ArrayList<String>,
                    @PathVariable tenantId: String,
                    @PathVariable conversationId: String,
                    @PathVariable turnId: String
                ): ResponseEntity<out String> {

                    val result = $agentRef.execute(
                        prepareInput(
                            turnMsg[0],
                            conversationId,
                            turnId,
                            tenantId
                        )
                    )
                    return ResponseEntity.ok(result.content)
                }

                private fun prepareInput(
                    message: String,
                    conversationID: String,
                    turnId: String,
                    tenantId: String
                ) = Input(
                    message,
                    RequestContext(conversationID, turnId, tenantId, RequestStatus.ONGOING),
                    mutableMapOf(
                        NATCO_CODE to "de",
                        USER to UserInformation(
                            accessToken = "accessToken",
                            profileId = "profileId"
                        ),
                        "question" to message,
                        "context" to "user",
                        "original_question" to message
                    )
                )
            }          
        """.trimIndent()
        writer.writeLine(packageDeclaration)
        writer.writeLine(consolidatedImports)
        writer.writeLine("import $packageName.*\n")

        writer.writeLine(classBody)
    }

    override fun createResponseStepCode(stepName: String, stepFile: File, packageName: String) {
        val writer = IndentedWriter(stepFile)

        stepFile.writeText("")

        val packageDeclaration = "package $packageName\n"
        val imports = lmosImports.getAsString(lmosImports.getLmosImportForStep())

        val classBody = """
            @Component
            class $stepName(
                private val languageModelExecutor: LanguageModelExecutor,
            ) : AbstractStep() {

                private val log = org.slf4j.LoggerFactory.getLogger(javaClass)

                companion object {
                    private const val CONTEXT_ORIGINAL_QUESTION = "original_question"
                    private const val CONTEXT_CLEANED_ANSWER = "cleaned_answer"
                }

                override suspend fun executeInternal(input: Input): Output {

                    val fullConversation = mutableListOf(
                        UserMessage(input.context<String>(GenerateResponse.CONTEXT_ORIGINAL_QUESTION))
                    )

                    val response = languageModelExecutor.ask(fullConversation, listOf()).getOrNull()

                    // Process the response
                    return processModelResponse(response, input)
                }

                private suspend fun processModelResponse(
                    response: AssistantMessage?,
                    input: Input,
                ): Output {
                    if (response == null) {
                        throw StepFailedException("Failed to generate response!")
                    }

                    input.stepContext[CONTEXT_CLEANED_ANSWER] = input.content

                    return Output(response.content, Status.CONTINUE, input)
                }
            }
        """.trimIndent()

        writer.writeLine(packageDeclaration)
        writer.writeLine(imports)
        writer.writeLine(classBody)
    }
}
