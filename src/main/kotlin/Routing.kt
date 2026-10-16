
package com.example

import com.example.application.SusuService
import com.example.domain.Member
import com.example.domain.Money
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*

@kotlinx.serialization.Serializable
data class CreateMemberRequest(
    val id: String,
    val name: String
)
@kotlinx.serialization.Serializable
data class MemberResponse(
    val id: String,
    val name: String
)

fun Application.configureRouting() {
    val susuService = SusuService(Money.fromCedis(50))

    routing {
        get("/") {
            call.respondText("Susu Manager API")
        }

        get("/api/v1/members") {
            val members = susuService.members().map { member ->
                MemberResponse(
                    id = member.id,
                    name = member.name
                )
            }

            call.respond(members)
        }

        post("/api/v1/members") {
            val request = call.receive<CreateMemberRequest>()

            try {
                susuService.addMember(
                    Member(
                        id = request.id,
                        name = request.name
                    )
                )
                call.respond(HttpStatusCode.Created)
            } catch (exception: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to (exception.message ?: "Invalid member"))
                )
            }
        }

//        get("/api/v1/members") {
//            call.respond(susuService.members())
//        }
    }
}
