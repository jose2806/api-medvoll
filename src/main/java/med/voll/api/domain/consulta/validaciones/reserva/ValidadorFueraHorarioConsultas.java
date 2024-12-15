package med.voll.api.domain.consulta.validaciones.reserva;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.DatosReservaConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorFueraHorarioConsultas implements ValidadorDeConsultas {

    public void validar(DatosReservaConsulta datos){
        var fechaConsulta = datos.fecha();
        var domingo = fechaConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var horarioAntesApertura = fechaConsulta.getHour() < 7;
        var horarioDespuesCierrre = fechaConsulta.getHour() > 18;
        if(domingo || horarioDespuesCierrre || horarioAntesApertura){
            throw new ValidacionException("Horario seleccionado esta fuera del horario de atencion");
        }
    }
}
