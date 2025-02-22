package com.telekom.iaplatformcli.generate.ne

class GradleSpringProjectFactory : ProjectFactory {
    override fun createBuildTool(): BuildTool {
        return GradleBuildTool()
    }

    override fun createFramework(): Framework {
        return SpringFramework()
    }
}