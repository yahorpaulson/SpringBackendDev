package yahor.backend.project_analysis.spring_boot_project.database.model


import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("notes")
data class Note (
    val title: String,
    val content: String,
    val color: Long,
    val createdAt: Instant = Instant.now(),
    val ownerId: ObjectId,
    @Id val id: ObjectId = ObjectId.get()
)