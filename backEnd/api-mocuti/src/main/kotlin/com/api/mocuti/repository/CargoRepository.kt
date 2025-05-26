package com.api.mocuti.repository

import com.api.mocuti.entity.Cargo
import org.springframework.data.jpa.repository.JpaRepository

interface CargoRepository : JpaRepository<Cargo, Int> {}