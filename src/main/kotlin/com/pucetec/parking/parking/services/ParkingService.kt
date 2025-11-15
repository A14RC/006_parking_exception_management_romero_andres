package com.pucetec.parking.parking.services

import com.pucetec.parking.parking.models.entities.ParkingEntry
import com.pucetec.parking.parking.repositories.ParkingEntryRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ParkingService(
    private val parkingEntryRepository: ParkingEntryRepository
) {

    fun registerEntry(entry: ParkingEntry): ParkingEntry {
        // Aquí irán las validaciones luego
        return parkingEntryRepository.save(entry)
    }

    fun findByPlate(plate: String): ParkingEntry {
        return parkingEntryRepository.findByPlate(plate)
            ?: throw Exception("Car not found") // luego lo reemplazamos
    }

    fun registerExit(plate: String) {
        val entry = parkingEntryRepository.findByPlate(plate)
            ?: throw Exception("Car not found") // luego lo reemplazamos

        parkingEntryRepository.delete(entry)
    }
}
