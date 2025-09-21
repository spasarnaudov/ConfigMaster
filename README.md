# ConfigMasterHelper

`ConfigMasterHelper` is a small utility library for integrating **ConfigMaster** into Android apps.  
It allows client applications (Demo apps) to **insert, update, and fetch configurations** from the central ConfigMaster app via a `ContentProvider`.

This library provides both:
- ✅ Kotlin implementation (`com.spascoding.configmasterhelper.ConfigMasterHelper`)
- ✅ Java implementation (`com.spascoding.configmasterhelper.javahelper.ConfigMasterHelper`)

---

## 📌 Features

- Insert/update configuration JSON into ConfigMaster
- Fetch a full configuration (as JSON string)
- Fetch a single parameter value by key from configuration JSON
- Safe package check — helper methods do nothing if `ConfigMaster` is not installed

---

## ⚙️ Setup Guide

To use **ConfigMasterHelper** in your Android project, follow these steps:

### 1. Add GitHub Maven Repository

In your **`settings.gradle.kts`**, ensure you have the GitHub Maven repository configured:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/spasarnaudov/ConfigMaster")
            credentials {
                username = localProperties.getProperty("gpr.user") ?: ""
                password = localProperties.getProperty("gpr.key") ?: ""
            }
        }
    }
}
```

### 2. Configure Credentials

Create or update the local.properties file in the root of your project and add:

```agsl
gpr.user=your_github_username
gpr.key=your_personal_access_token
```


Replace your_github_username with your GitHub username.

Replace your_personal_access_token with a GitHub Personal Access Token (PAT) that has at least read:packages permission.

⚠️ Do not commit local.properties to version control — it contains secrets.

---

### 3. Add Dependency

In your app/build.gradle.kts, add the dependency:

```agsl
dependencies {
    implementation("com.spascoding:configmasterhelper:0.0.3")
}
```

### 4. Update Manifest

In your AndroidManifest.xml, declare the ConfigMaster package and provider (needed for queries):

```agsl
<queries>
    <package android:name="com.spascoding.configmaster" />
    <provider android:authorities="com.spascoding.configmaster.data.provider.ConfigProvider" />
</queries>
```

✅ Now you’re ready to use ConfigMasterHelper in your project!

---

## 🛠 Usage

### Kotlin Example
```agsl
import com.spascoding.configmasterhelper.ConfigMasterHelper

// Insert configuration
ConfigMasterHelper.insertConfig(
    context = this,
    configName = "demoAppConfig",
    jsonData = """{"cards": "5", "theme": "dark"}"""
)

// Fetch full config
val jsonConfig = ConfigMasterHelper.fetchConfig(this, "demoAppConfig")
println("Full JSON: $jsonConfig")

// Fetch only one parameter
val cardsCount = ConfigMasterHelper.fetchConfigParam(this, "demoAppConfig", "cards")
println("Cards count = $cardsCount")
```

### Java Example
```agsl
import com.spascoding.configmasterhelper.javahelper.ConfigMasterHelper;

// Insert configuration
ConfigMasterHelper.insertConfig(
    context,
    "demoAppConfig",
    "{\"cards\": \"5\", \"theme\": \"dark\"}"
);

// Fetch full config
String jsonConfig = ConfigMasterHelper.fetchConfig(context, "demoAppConfig");
Log.d("Demo", "Full JSON: " + jsonConfig);

// Fetch only one parameter
String cardsCount = ConfigMasterHelper.fetchConfigParam(context, "demoAppConfig", "cards");
Log.d("Demo", "Cards count = " + cardsCount);
```

---

## 🔍 API Reference

### Kotlin: `ConfigMasterHelper`

| Function | Description |
|----------|-------------|
| `insertConfig(context, configName, jsonData)` | Insert or update a configuration (JSON string). |
| `fetchConfig(context, configName): String?` | Fetch full configuration as JSON. |
| `fetchConfigParam(context, configName, paramKey): String?` | Fetch one parameter value from config JSON. |

---

### Java: `ConfigMasterHelper`

| Function | Description |
|----------|-------------|
| `insertConfig(Context, String, String)` | Insert or update a configuration (JSON string). |
| `fetchConfig(Context, String)` | Fetch full configuration as JSON. |
| `fetchConfigParam(Context, String, String)` | Fetch one parameter value from config JSON. |

---

## 📦 Notes

- Configurations are stored in the **ConfigMaster** app, not inside the demo app.
- Demo apps communicate with ConfigMaster through its `ContentProvider`:

```content://com.spascoding.configmaster.data.provider.ConfigProvider```

---