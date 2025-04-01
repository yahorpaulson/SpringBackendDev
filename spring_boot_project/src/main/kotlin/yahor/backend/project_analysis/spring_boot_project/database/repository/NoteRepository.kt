package yahor.backend.project_analysis.spring_boot_project.database.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import yahor.backend.project_analysis.spring_boot_project.database.model.Note

interface NoteRepository: MongoRepository<Note, ObjectId>{
    fun findByOwnerId(ownerId: ObjectId,):List<Note>
}


