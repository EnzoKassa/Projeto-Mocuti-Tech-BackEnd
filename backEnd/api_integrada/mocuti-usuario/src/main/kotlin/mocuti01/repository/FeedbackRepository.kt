package mocuti01.repository

import org.springframework.data.jpa.repository.JpaRepository
import mocuti01.entity.Feedback

interface FeedbackRepository : JpaRepository<Feedback, Int> {


}