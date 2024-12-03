package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    @Transactional
    public void registrarPaciente(@RequestBody @Valid DatosRegistroPaciente datosRegistroPaciente){
        pacienteRepository.save(new Paciente(datosRegistroPaciente));
    }

    @GetMapping
    public Page<DatosListaPaciente> listarPacientes(@PageableDefault(page = 0, size = 10, sort = {"nombre"}) Pageable paginacion) {
        return pacienteRepository.findAll(paginacion).map(DatosListaPaciente::new);
    }

    @PutMapping
    @Transactional
    public void actualizarPaciente(@RequestBody @Valid DatosActualizarPaciente datosActualizarPaciente){
        Paciente paciente = pacienteRepository.getReferenceById(datosActualizarPaciente.id());
        paciente.actualizarDatos(datosActualizarPaciente);
    }

    @DeleteMapping("{id}")
    @Transactional
    public void eliminarPaciente(@PathVariable Long id){
        Paciente paciente = pacienteRepository.getReferenceById(id);
        paciente.desactivarPaciente();
    }
}
