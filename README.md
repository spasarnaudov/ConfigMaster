# ConfigMaster SDK

ConfigMaster SDK is a lightweight configuration manager for Android apps.  
Each host app has its own instance of **ConfigMasterSDK** with its own local database.

---

## 📌 Features

- Store and read configurations as JSON
- Works in **Hilt and non-Hilt apps**
- Supports **suspend (coroutines)** and **async (callback)** APIs

---

## ⚙️ Setup Guide

1. Add SDK module to your project:

```gradle
implementation("com.spascoding:config-master-sdk:<VERSION>")
```
2. Initialize in your Application **(only for non-hilt host app)**:

```kotlin
class HostApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ConfigMasterSdk.initialize(this) //You don't need from this for hilt projects
    }
}
```

---

## 🛠 Usage

### Insert configuration JSON

#### Kotlin
```kotlin
ConfigMasterSdk.insertJsonAsync("config_name", jsonString) // Async
lifecycleScope.launch { ConfigMasterSdk.insertJson("config_name", jsonString) } // Suspend
```

#### Java
```java
// Async
ConfigMasterSdk.insertJsonAsync("config_name", jsonString);

// Suspend equivalent (using coroutine in Kotlin only), Java would typically use AsyncTask or Executor
```

### Read full configuration

#### Kotlin
```kotlin
ConfigMasterSdk.getModifiedJsonAsync("config_name") { json -> /* use json */ } // Async
lifecycleScope.launch { val json = ConfigMasterSdk.getModifiedJson("config_name") } // Suspend
```

#### java
```java
ConfigMasterSdk.getModifiedJsonAsync("config_name", json -> {
    // use json
});
```

### Open ConfigMasterActivity to edit configuration

#### Kotlin
```kotlin
val intent = Intent(applicationContext, ConfigMasterActivity::class.java)
intent.putExtra(ConfigMasterActivity.EXTRA_TITLE, "config_master_name")
startActivity(intent)
```

#### Java
```java
Intent intent = new Intent(this, ConfigMasterActivity.class);
intent.putExtra(ConfigMasterActivity.EXTRA_TITLE, "config_master_name");
startActivity(intent);
```
---

# ConfigMasterHelper

`ConfigMasterHelper` is a small utility library for integrating **ConfigMaster** into Android apps.  
It allows host applications to **insert, update, and fetch configurations** from the central ConfigMaster app via a `ContentProvider`.

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

### 1. Add Dependency

In your app/build.gradle.kts, add the dependency:

```agsl
dependencies {
    implementation("com.spascoding:config-master-helper:<VERSION>")
}
```

### 2. Update Manifest

In your AndroidManifest.xml, declare the ConfigMaster package and provider (needed for queries):

```agsl
<queries>
    <package android:name="com.spascoding.configmaster" />
    <provider android:authorities="com.spascoding.configmaster.data.provider.ConfigProvider" />
</queries>
```

---

## 🛠 Usage

### Kotlin Example
```agsl
import com.spascoding.configmasterhelper.ConfigMasterHelper

// Insert configuration
ConfigMasterHelper.insertConfig(
    context = this,
    configName = "config_name",
    jsonData = """{"cards": "5", "theme": "dark"}"""
)

// Fetch full config
val jsonConfig = ConfigMasterHelper.fetchConfig(this, "config_name")
println("Full JSON: $jsonConfig")

// Fetch only one parameter
val cardsCount = ConfigMasterHelper.fetchConfigParam(this, "config_name", "cards")
println("Cards count = $cardsCount")
```

### Java Example
```agsl
import com.spascoding.configmasterhelper.javahelper.ConfigMasterHelper;

// Insert configuration
ConfigMasterHelper.insertConfig(
    context,
    "config_name",
    "{\"cards\": \"5\", \"theme\": \"dark\"}"
);

// Fetch full config
String jsonConfig = ConfigMasterHelper.fetchConfig(context, "config_name");
Log.d("Demo", "Full JSON: " + jsonConfig);

// Fetch only one parameter
String cardsCount = ConfigMasterHelper.fetchConfigParam(context, "config_name", "cards");
Log.d("Demo", "Cards count = " + cardsCount);
```