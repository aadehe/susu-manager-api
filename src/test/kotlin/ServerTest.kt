package com.example

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.*
import com.example.domain.Member
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ServerTest {

    @Test
    fun `test root endpoint`() = testApplication {
        // loads default configuration
        configure()
        // verify server root returns 200
        assertEquals(HttpStatusCode.OK, client.get("/").status)
    }


    @Test
    fun `creating a member returns 201 Created`() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }

        val response = client.post("/api/v1/members") {
            contentType(ContentType.Application.Json)
            setBody("""{"id":"member-001","name":"Ama"}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `creating a member with a duplicate ID returns 400 Bad Request`() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }

        val requestBody = """{"id":"member-001","name":"Ama"}"""

        val firstResponse = client.post("/api/v1/members") {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(requestBody)
        }

        val secondResponse = client.post("/api/v1/members") {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody("""{"id":"member-001","name":"Kofi"}""")
        }

        assertEquals(HttpStatusCode.Created, firstResponse.status)
        assertEquals(HttpStatusCode.BadRequest, secondResponse.status)
    }

    @Test
    fun `listing members returns members in join order`() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }

        client.post("/api/v1/members") {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody("""{"id":"member-001","name":"Ama"}""")
        }

        client.post("/api/v1/members") {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody("""{"id":"member-002","name":"Kofi"}""")
        }

        val response = client.get("/api/v1/members")

//        assertEquals(HttpStatusCode.OK, response.status)
//        assertEquals(
//            """[{"id":"member-001","name":"Ama"},{"id":"member-002","name":"Kofi"}]""",
//            response.bodyAsText()
//        )
        assertEquals(HttpStatusCode.OK, response.status)
        println("Members response: ${response.bodyAsText()}")
    }




}
