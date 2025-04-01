package yahor.backend.project_analysis.spring_boot_project.controllers

import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yahor.backend.project_analysis.spring_boot_project.database.model.Note
import yahor.backend.project_analysis.spring_boot_project.database.repository.NoteRepository
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController (
    private val repository: NoteRepository
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
    fun save(
        @RequestBody body: NoteRequest
    ):NoteResponse{
        val note = repository.save(
            Note(
                id = body.id?.let { ObjectId(it) }?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ObjectId()

            )
        )
        return  note.toResponse()

    }
    @GetMapping
    fun findByOwnerId(
        @RequestParam(required = true) ownerId: String
    ):List<NoteResponse>{
        return repository.findByOwnerId(ObjectId()).map {
            it.toResponse()
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