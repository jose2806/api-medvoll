package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    @Transactional
    public ResponseEntity registrarPaciente(@RequestBody @Valid DatosRegistroPaciente datosRegistroPaciente, UriComponentsBuilder uriBuilder){
        Paciente paciente = pacienteRepository.save(new Paciente(datosRegistroPaciente));
        URI uri = uriBuilder.path("/paciente/{id}").buildAndExpand(paciente.getId()).toUri();
        DatosRespuestaPaciente datosRespuestaPaciente = new DatosRespuestaPaciente(
                paciente.getId(), paciente.getNombre(), paciente.getEmail(), paciente.getTelefono(),
                paciente.getDocumento(), new DatosDireccion(paciente.getDireccion().getCalle(),
                paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(),
                paciente.getDireccion().getNumero(), paciente.getDireccion().getComplemento()
        ));
        return  ResponseEntity.created(uri).body(datosRespuestaPaciente);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoPaciente>> listarPacientes(@PageableDefault(page = 0, size = 10, sort = {"nombre"}) Pageable paginacion) {
        return ResponseEntity.ok(pacienteRepository.findAll(paginacion)
                .map(DatosListadoPaciente::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarPaciente(@RequestBody @Valid DatosActualizarPaciente datosActualizarPaciente){
        Paciente paciente = pacienteRepository.getReferenceById(datosActualizarPaciente.id());
        paciente.actualizarDatos(datosActualizarPaciente);
        return ResponseEntity.ok(new DatosRespuestaPaciente(
                paciente.getId(), paciente.getNombre(), paciente.getEmail(), paciente.getTelefono(),
                paciente.getDocumento(), new DatosDireccion(paciente.getDireccion().getCalle(),
                paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(),
                paciente.getDireccion().getNumero(), paciente.getDireccion().getComplemento()
        )));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarPaciente(@PathVariable Long id){
        Paciente paciente = pacienteRepository.getReferenceById(id);
        paciente.desactivarPaciente();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaPaciente> retornarPaciente(@PathVariable Long id){
        Paciente paciente = pacienteRepository.getReferenceById(id);
        var datosPaciente = new DatosRespuestaPaciente(
                paciente.getId(), paciente.getNombre(), paciente.getEmail(),
                paciente.getTelefono(), paciente.getDocumento(), new DatosDireccion(
                paciente.getDireccion().getCalle(), paciente.getDireccion().getDistrito(),
                paciente.getDireccion().getCiudad(), paciente.getDireccion().getNumero(),
                paciente.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosPaciente);
    }
}
