package ru.sevavto.stock.crm.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_manager_id", referencedColumnName = "id")
    private OrganizationManager organizationManager;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderPosition> positions;

    private BigDecimal totalPrice;

    @CreationTimestamp
    private LocalDate dateOfOrder;

    @UpdateTimestamp
    private LocalDate dateOfStatusUpdate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String deliveryAddress;
}
