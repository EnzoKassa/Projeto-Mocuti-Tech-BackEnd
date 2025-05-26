package com.api.mocuti.repository

import com.api.mocuti.entity.NotaFeedback
import org.springframework.data.jpa.repository.JpaRepository

interface NotaFeedbackRepository : JpaRepository<NotaFeedback, Int> {
}