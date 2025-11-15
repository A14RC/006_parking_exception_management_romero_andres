package com.pucetec.parking.parking.services

import com.pucetec.parking.parking.models.entities.ParkingEntry
import com.pucetec.parking.parking.repositories.ParkingEntryRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class ParkingEntryService(
    private val parkingEntryRepository: ParkingEntryRepository
) {

    fun createEntry(entry: ParkingEntry): ParkingEntry {
        // Validar formato
        val plateRegex = Regex("^[A-Z]{3}-\\d{4}\$")
        if (!plateRegex.matches(entry.plate)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid plate format: ${entry.plate}")
        }

        // Lista negra
        val blacklist = listOf("AAA-0001", "BBB-0002")
        if (blacklist.contains(entry.plate)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "This plate is blacklisted: ${entry.plate}")
        }

        // Capacidad máxima
        val activeCars = parkingEntryRepository.findAll().count { it.exitTime == null }
        if (activeCars >= 20) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Parking is full. Cannot add plate ${entry.plate}")
        }

        // Duplicados
        val existing = parkingEntryRepository.findByPlateAndExitTimeIsNull(entry.plate)
        if (existing != null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle with plate ${entry.plate} already entered.")
        }

        entry.entryTime = LocalDateTime.now()
        return parkingEntryRepository.save(entry)
    }


    fun getAllEntries(): List<ParkingEntry> {
        return parkingEntryRepository.findAll()
    }

    fun getEntryById(id: Long): ParkingEntry {
        return parkingEntryRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Entry with id $id not found.") }
    }

    fun findByPlate(plate: String): ParkingEntry {
        return parkingEntryRepository.findByPlate(plate)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle with plate $plate not found.")
    }

    fun registerExit(plate: String): ParkingEntry {
        val entry = parkingEntryRepository.findByPlateAndExitTimeIsNull(plate)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No active entry found for plate $plate.")

        val now = LocalDateTime.now()
        if (entry.entryTime.plusHours(8).isBefore(now)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Parking time exceeded 8 hours for plate ${plate}")
        }

        entry.exitTime = now
        return parkingEntryRepository.save(entry)
    }


    fun deleteEntry(id: Long) {
        val entry = parkingEntryRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Entry with id $id not found.") }
        parkingEntryRepository.delete(entry)
    }
}
