package com.api.mocuti.repository

import com.api.mocuti.entity.VisaoGeralUsuariosView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VisaoGeralUsuariosRepository : JpaRepository<VisaoGeralUsuariosView, Int>
