package com.api.mocuti.repository

import com.api.mocuti.entity.Feedback
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackRepository : JpaRepository<Feedback, Int> {


}