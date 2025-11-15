package com.pucetec.parking.parking.controllers

import com.pucetec.parking.parking.models.entities.ParkingEntry
import com.pucetec.parking.parking.services.ParkingEntryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/parking")
class ParkingEntryController(
    private val parkingEntryService: ParkingEntryService
) {

    @PostMapping
    fun createEntry(@RequestBody entry: ParkingEntry): ParkingEntry {
        return parkingEntryService.createEntry(entry)
    }

    @GetMapping
    fun getAllEntries(): List<ParkingEntry> {
        return parkingEntryService.getAllEntries()
    }

    @GetMapping("/{id}")
    fun getEntryById(@PathVariable id: Long): ParkingEntry {
        return parkingEntryService.getEntryById(id)
    }

    @PostMapping("/exit/{plate}")
    fun registerExit(@PathVariable plate: String): ParkingEntry {
        return parkingEntryService.registerExit(plate)
    }

    @DeleteMapping("/{id}")
    fun deleteEntry(@PathVariable id: Long): String {
        parkingEntryService.deleteEntry(id)
        return "Entry with id $id deleted successfully."
    }
}
