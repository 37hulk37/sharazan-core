# sharazan-core

**Sharazan** — модульный бэкенд-фреймворк на Kotlin, вдохновлённый архитектурой Ktor (declare-then-start композиция через Koin, без Spring-магии).

**core** — application bootstrap / composition root: `AppBuilder` (аккумулятор Koin-модулей), `Application`/`Lifecycle`, обобщённый request/response `pipeline` (`Interceptor`/`Phase`/`Pipeline`), request-scoped context-хелперы и загрузка конфигурации из property-файлов.

## Стек

- Koin (DI)
- http4k-core (типы `Request`/`Response`/`Uri`)
- kotlinx-serialization-properties

## Maven-координаты

```kotlin
implementation("com.github.37hulk37:sharazan-core:1.0.0")
```
