package eu.heha.fotocleaner

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform