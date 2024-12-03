package med.voll.api.paciente;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.direccion.Direccion;

@Table(name="pacientes")
@Entity(name="Paciente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String documento;
    private String telefono;

    @Embedded
    private Direccion direccion;
    private Boolean activo;

    public Paciente(DatosRegistroPaciente datos){
        this.nombre = datos.nombre();
        this.email = datos.email();
        this.telefono = datos.telefono();
        this.documento = datos.documento();
        this.direccion = new Direccion(datos.direccion());
        this.activo = true;
    }

    public void actualizarDatos(DatosActualizarPaciente datosActualizarPaciente) {
        if(datosActualizarPaciente.nombre() != null){
            this.nombre = datosActualizarPaciente.nombre();
        }
        if(datosActualizarPaciente.telefono() != null){
            this.telefono = datosActualizarPaciente.telefono();
        }
        if(datosActualizarPaciente.direccion() != null){
            this.direccion = direccion.actualizarDatos(datosActualizarPaciente.direccion());
        }
    }

    public void desactivarPaciente(){
        this.activo = false;
    }
}
