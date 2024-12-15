package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.validaciones.cancelamiento.ValidadorCancelamientoDeConsulta;
import med.voll.api.domain.consulta.validaciones.reserva.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaDeConsultas {
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidadorDeConsultas> validadores;

    @Autowired
    private List<ValidadorCancelamientoDeConsulta> validadoresCancelamiento;

    public DatosDetalleConsulta reservar(DatosReservaConsulta datos){

        if(!pacienteRepository.existsById(datos.idPaciente())){
            throw new ValidacionException("No existe un paciente con el id recibido");
        }

        if(datos.idMedico() != null && !medicoRepository.existsById(datos.idMedico())){
            throw new ValidacionException("No existe un medico con el id recibido");
        }
        validadores.forEach(v->v.validar(datos));
        var medico = elegirMedico(datos);
        if(medico == null){
            throw new ValidacionException("No existe medico disponible en ese horario");
        }
        var paciente = pacienteRepository.findById(datos.idPaciente()).get();
        var consulta = new Consulta(null,medico,paciente,datos.fecha(),null);
        consultaRepository.save(consulta);
        return new DatosDetalleConsulta(consulta);
    }

    private Medico elegirMedico(DatosReservaConsulta datos) {
        if (datos.idMedico() != null){
            medicoRepository.getReferenceById(datos.idMedico());
        }
        if (datos.especialidad() == null){
            throw new ValidacionException("Es necesario elegir especialidad cuando no se elige un medico");
        }
        return medicoRepository.elegirMedicoAleatorioDisponibleEnLaFecha(datos.especialidad(),datos.fecha());
    }

    public void cancelar(DatosCancelamientoConsulta datos){
        if (!consultaRepository.existsById(datos.idConsulta())){
            throw new ValidacionException("Id de la consulta no existe!");
        }
        validadoresCancelamiento.forEach(v->v.validar(datos));
        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        consulta.cancelar(datos.motivo());
    }
}
