# Add project specific ProGuard rules here.
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses

# Ktor
-keep class io.ktor.** { *; }
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** { kotlinx.serialization.KSerializer serializer(...); }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# DataStore
-keep class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite

# Keep data classes for serialization
-keepclassmembers class com.inventario.app.data.remote.dto.** { *; }
-keepclassmembers class com.inventario.app.domain.model.** { *; }
