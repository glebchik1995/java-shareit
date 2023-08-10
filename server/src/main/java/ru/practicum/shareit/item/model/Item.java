package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Builder
@Entity
@Setter
@Getter
@Table(name = "items", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "name", nullable = false, length = 100)
    String name;
    @Column(name = "description", nullable = false, length = 500)
    String description;
    @Column(name = "is_available", nullable = false)
    Boolean available;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest itemRequest;
}
