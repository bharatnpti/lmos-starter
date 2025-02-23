package org.eclipse.lmos.starter.factory

import org.eclipse.lmos.starter.buildtool.GradleBuildTool
import org.eclipse.lmos.starter.framework.SpringFramework

class GradleSpringProjectFactory : ProjectFactory {
    override fun createBuildTool(): BuildTool {
        return GradleBuildTool()
    }

    override fun createFramework(): Framework {
        return SpringFramework()
    }
}