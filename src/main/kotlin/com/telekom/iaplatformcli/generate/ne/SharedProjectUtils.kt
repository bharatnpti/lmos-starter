package com.telekom.iaplatformcli.generate.ne

import java.nio.file.Path


fun resolveSrcPath(projectDir: Path): Path = projectDir.resolve("src").resolve("main").resolve("kotlin")