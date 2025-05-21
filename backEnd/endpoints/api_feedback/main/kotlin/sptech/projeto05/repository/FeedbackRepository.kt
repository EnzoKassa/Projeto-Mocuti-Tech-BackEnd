package sptech.projeto05.repository

import org.springframework.data.jpa.repository.JpaRepository
import sptech.projeto05.entity.Feedback

interface FeedbackRepository : JpaRepository<Feedback, Int> {


}