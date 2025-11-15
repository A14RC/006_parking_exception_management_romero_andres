package com.pucetec.parking.parking.repositories

import com.pucetec.parking.parking.models.entities.ParkingEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParkingEntryRepository : JpaRepository<ParkingEntry, Long> {

    fun findByPlate(plate: String): ParkingEntry?
}
