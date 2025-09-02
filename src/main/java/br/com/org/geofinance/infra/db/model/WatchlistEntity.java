package br.com.org.geofinance.infra.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "watchlist")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchlistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ex.: "PETR4.SA"
    @Column(name = "symbol", nullable = false, length = 50)
    private String symbol;

    // IBGE city id
    @Column(name = "city_id", nullable = true)
    private Integer cityId;

    @Column(name = "target_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal targetPrice;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}

