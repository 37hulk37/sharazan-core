package com.sharazan.core.source

private val ENV_PLACEHOLDER = Regex("""\$\{([A-Za-z0-9_.]+)(?::([^}]*))?}""")

fun resolveEnvPlaceholders(text: String): String = ENV_PLACEHOLDER.replace(text) { match ->
    val name = match.groupValues[1]
    val hasDefault = match.groups[2] != null
    val default = match.groupValues[2]

    System.getenv(name)
        ?: if (hasDefault) default else error("Missing env var '$name' and no default provided in configuration")
}
