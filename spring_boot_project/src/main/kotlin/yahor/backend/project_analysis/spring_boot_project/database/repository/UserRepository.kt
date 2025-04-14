import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import yahor.backend.project_analysis.spring_boot_project.database.model.User

interface UserRepository: MongoRepository<User, ObjectId> {
    fun findByEmail(email: String): User?

}