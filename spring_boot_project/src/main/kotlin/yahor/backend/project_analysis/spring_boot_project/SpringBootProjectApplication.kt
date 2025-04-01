package yahor.backend.project_analysis.spring_boot_project

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootProjectApplication

fun main(args: Array<String>) {
	runApplication<SpringBootProjectApplication>(*args)
}
