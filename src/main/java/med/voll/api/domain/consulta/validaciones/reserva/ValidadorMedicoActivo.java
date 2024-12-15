package med.voll.api.domain.consulta.validaciones.reserva;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.DatosReservaConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoActivo implements ValidadorDeConsultas {
    @Autowired
    private MedicoRepository repository;

    public void validar(DatosReservaConsulta datos){
        if(datos.idMedico() == null){
            return;
        }
        var medicoActivo = repository.findActivoById(datos.idMedico());
        if (!medicoActivo){
            throw new ValidacionException("Consulta no puede ser reservada con medico excluido");
        }
    }
}
