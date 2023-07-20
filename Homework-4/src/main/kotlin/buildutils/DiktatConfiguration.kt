/**
 * Configuration for diktat static analysis
 */

package buildutils

import org.cqfn.diktat.plugin.gradle.DiktatExtension
import org.cqfn.diktat.plugin.gradle.DiktatGradlePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

/**
 * Applies diktat gradle plugin and configures diktat for [this] project
 */
fun Project.configureDiktat() {
    apply<DiktatGradlePlugin>()
    configure<DiktatExtension> {
        inputs {
            include("src/main/**/*.kt")
        }
    }
}

/**
 * Creates unified tasks to run diktat on all projects
 */
fun Project.createDiktatTask() {
    if (this == rootProject) {
        apply<DiktatGradlePlugin>()
        configure<DiktatExtension> {
            diktatConfigFile = rootProject.file("diktat.yml")
            inputs {
                include("./*.kts")
                include("./buildSrc/**/*.kt")
            }
        }
    }
    tasks.register("diktatCheckAll") {
        group = "verification"
        allprojects {
            this@register.dependsOn(tasks.getByName("diktatCheck"))
        }
    }
    tasks.register("diktatFixAll") {
        group = "verification"
        allprojects {
            this@register.dependsOn(tasks.getByName("diktatFix"))
        }
    }
}
