package yahor.backend.project_analysis.spring_boot_project.controllers

import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yahor.backend.project_analysis.spring_boot_project.database.model.Note
import yahor.backend.project_analysis.spring_boot_project.database.repository.NoteRepository
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController(
    private val repository: NoteRepository,
    private val noteRepository: NoteRepository
){
    data class NoteRequest(
        val id: String?,
        val title: String,
        val content: String,
        val color: Long
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant
    )

    @PostMapping
    fun save(@RequestBody body: NoteRequest): NoteResponse {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val id = if (body.id != null) {
            if (ObjectId.isValid(body.id)) ObjectId(body.id)
            else throw IllegalArgumentException("Incorrect ID: ${body.id}")
        } else {
            ObjectId.get()
        }

        val note = repository.save(
            Note(
                id = id,
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ObjectId(ownerId)
            )
        )
        return note.toResponse()
    }


    @GetMapping
    fun findByOwnerId(): List<NoteResponse> {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return repository.findByOwnerId(ObjectId(ownerId)).map {
            it.toResponse()
        }
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(@PathVariable id: String) {
        if (!ObjectId.isValid(id)) {
            throw IllegalArgumentException("Wrong format id: $id")
        }

        val objectId = ObjectId(id)
        val note = noteRepository.findById(objectId)
            .orElseThrow { IllegalArgumentException("Note not found") }

        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        if (note.ownerId.toHexString() == ownerId) {
            repository.deleteById(objectId)
        }
    }





    private fun Note.toResponse(): NoteController.NoteResponse{
        return NoteResponse(
            id = id.toHexString(),
            title = title,
            content = content,
            color = color,
            createdAt = createdAt

        )
    }

}