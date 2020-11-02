package com.trabalho.verival.clinicamedica;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClinicamedicaApplicationTests {

	@Test
	void test1() {
		Medico medico = new Medico("José", "12345", Especialidade.CIRURGIAO);
		Sala sala = new Sala("Sala 1", TipoSala.SALA_PEQUENA);

		Reserva reserva = new Reserva(medico, sala,
				LocalDateTime.now().plusDays(1).plusHours(2),
				LocalDateTime.now().plusDays(1).plusHours(5)))

	}
}
