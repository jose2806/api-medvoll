package med.voll.api.controller;

import med.voll.api.domain.consulta.DatosDetalleConsulta;
import med.voll.api.domain.consulta.DatosReservaConsulta;
import med.voll.api.domain.consulta.ReservaDeConsultas;
import med.voll.api.domain.medico.Especialidad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<DatosReservaConsulta> datosReservaConsultaJson;
    @Autowired
    private JacksonTester<DatosDetalleConsulta> datosDetalleConsultaJson;

    //@MockBean
    @MockitoBean
    private ReservaDeConsultas reservaDeConsultas;

    @Test
    @DisplayName("Deberia devolver http 400 cuando el rquest no tenga datos")
    @WithMockUser
    void reservarEscenario1() throws Exception {
        var response = mockMvc.perform(post("/consultas"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deberia devolver http 200 cuando el rquest reciba un json valido")
    @WithMockUser
    void reservarEscenario2() throws Exception {
        var fecha = LocalDateTime.now().plusHours(1);
        var especialidad = Especialidad.CARDIOLOGIA;
        var datosDetalle = new DatosDetalleConsulta(null,2l,5l,fecha);
        when(reservaDeConsultas.reservar(any())).thenReturn(datosDetalle);

        var response = mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(datosReservaConsultaJson.write(
                                new DatosReservaConsulta(2l,5l,fecha,especialidad)
                        ).getJson()))
                .andReturn().getResponse();

        var jsonEsperado = datosDetalleConsultaJson.write(datosDetalle).getJson();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
}