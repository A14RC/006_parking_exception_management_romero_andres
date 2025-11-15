package com.pucetec.parking.parking.models.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "parking_entries")
data class ParkingEntry(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val plate: String,

    @Column(nullable = false)
    val ownerName: String,

    @Column(nullable = false)
    var entryTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = true)
    var exitTime: LocalDateTime? = null
)
