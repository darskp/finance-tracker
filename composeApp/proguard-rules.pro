# Kotlinx Serialization
-keep class kotlinx.serialization.** { *; }
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep all @Serializable classes (NavRoutes, data models)
-keep @kotlinx.serialization.Serializable class ** { *; }
-keepclassmembers @kotlinx.serialization.Serializable class ** { *; }

# Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Room
-keep class androidx.room.** { *; }

# Koin DI
-keep class org.koin.** { *; }
-keep class org.koin.core.** { *; }
-dontwarn org.koin.**

# Coil image loader
-keep class coil3.** { *; }
-dontwarn coil3.**

# App model and UI classes
-keep class com.finvoraai.** { *; }

# Keep serialization annotations
-keepattributes *Annotation*

# Keep Kotlin metadata for reflection
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Prevent warnings
-dontwarn kotlinx.serialization.**
-dontwarn kotlin.**
-dontwarn kotlinx.coroutines.**
