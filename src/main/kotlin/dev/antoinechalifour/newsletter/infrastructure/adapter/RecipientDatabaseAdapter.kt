package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.domain.Recipient
import dev.antoinechalifour.newsletter.domain.RecipientPort
import dev.antoinechalifour.newsletter.infrastructure.database.RecipientDatabase
import dev.antoinechalifour.newsletter.infrastructure.database.RecipientRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RecipientDatabaseAdapter(val recipientRepository: RecipientRepository) : RecipientPort {
    override fun ofId(id: UUID): Recipient = recipientRepository.findById(id)
        .map { it.toRecipient() }
        .orElseThrow()

    override fun save(recipient: Recipient) {
        recipientRepository.save(RecipientDatabase.of(recipient))
    }
}