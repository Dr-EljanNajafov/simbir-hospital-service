package com.github.simbir_hospital_service.hospital;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "contact_phone", nullable = false)
    private String contactPhone;

    @Column(name = "room")
    @ElementCollection
    @CollectionTable(name = "hospital_rooms", joinColumns = @JoinColumn(name = "hospital_id"))
    private List<String> rooms;
}
