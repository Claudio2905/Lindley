package utp.sistemalindley.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ingreso_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngresoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingreso")
    private Integer idIngreso;

    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;

    @Column(name = "id_almacen", nullable = false)
    private Integer idAlmacen;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(length = 50)
    private String lote;

    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso;

    @Column(length = 100)
    private String ubicacion;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @PrePersist
    public void prePersist() {
        if (this.fechaIngreso == null) {
            this.fechaIngreso = LocalDateTime.now();
        }
    }
}
