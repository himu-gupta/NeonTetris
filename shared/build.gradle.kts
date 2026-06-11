import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.kotlin.multiplatform.library)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  android {
    namespace = "com.himugupta.neontetris.shared"
    compileSdk = 36
    minSdk = 26

    withHostTestBuilder {}

    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_17)
    }
  }

  jvm("desktop")

  val iosTargets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())
  iosTargets.forEach { target ->
    target.binaries.framework {
      baseName = "NeonTetrisShared"
      isStatic = true
    }
  }

  sourceSets {
    commonMain.dependencies {
      api(compose.runtime)
      api(compose.foundation)
      api(compose.material3)
      api(compose.ui)
      implementation(compose.materialIconsExtended)
      implementation(compose.components.uiToolingPreview)
      implementation(libs.kotlinx.coroutines.core)
      implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:2.9.6")
      implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.6")
    }

    androidMain.dependencies {
      implementation(libs.androidx.activity.compose)
    }

    getByName("desktopMain").dependencies {
      implementation(compose.desktop.currentOs)
      implementation(libs.kotlinx.coroutines.swing)
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
    }
  }
}

compose.desktop {
  application {
    mainClass = "com.himugupta.neontetris.desktop.MainKt"
    nativeDistributions {
      targetFormats(TargetFormat.Dmg)
      packageName = "Neon Tetris"
      packageVersion = "1.0.0"
    }
  }
}
