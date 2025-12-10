package utp.sistemalindley.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empleado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, length = 50)
    private String cargo;

    @Column(length = 20)
    private String estado = "Activo";

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Transient
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}
