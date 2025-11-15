package com.pucetec.parking.parking.services

import com.pucetec.parking.parking.models.entities.ParkingEntry
import com.pucetec.parking.parking.repositories.ParkingEntryRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ParkingEntryService(
    private val parkingEntryRepository: ParkingEntryRepository
) {

    fun createEntry(entry: ParkingEntry): ParkingEntry {
        return parkingEntryRepository.save(entry)
    }

    fun getAllEntries(): List<ParkingEntry> {
        return parkingEntryRepository.findAll()
    }

    fun getEntryById(id: Long): ParkingEntry? {
        return parkingEntryRepository.findById(id).orElse(null)
    }

    fun findByPlate(plate: String): ParkingEntry {
        return parkingEntryRepository.findByPlate(plate)
            ?: throw Exception("Car not found")
    }



    fun registerExit(plate: String): ParkingEntry {
        val entry = parkingEntryRepository.findByPlate(plate)
            ?: throw Exception("Car not found")

        if (entry.exitTime != null) {
            throw Exception("Exit already registered for this car")
        }

        entry.exitTime = LocalDateTime.now()
        return parkingEntryRepository.save(entry)
    }


}
