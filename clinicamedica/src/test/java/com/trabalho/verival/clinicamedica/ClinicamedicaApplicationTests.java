package com.trabalho.verival.clinicamedica;

import com.trabalho.verival.clinicamedica.entities.*;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.trabalho.verival.clinicamedica.controllers.ReservaController.isReservaValid;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ClinicamedicaApplicationTests {

	// Validação de Cirurgião com sala pequena:
	@Test
	void test1() {
		Medico medico = new Medico("Dr. José", "12345", Especialidade.CIRURGIAO);
		Sala sala = new Sala("Sala 1", TipoSala.SALA_PEQUENA);

		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		LocalDateTime horaInicio = formater.parseLocalDateTime("2020-11-30 19:00");
		LocalDateTime horaFinal = formater.parseLocalDateTime("2020-11-30 22:00");

		Reserva reserva = new Reserva(medico, sala, horaInicio, horaFinal);
		assertEquals("Cirurgiões e neurologistas não podem usar salas pequenas", isReservaValid(reserva));
	}

	// Validação Dermatologista na sala grande:
	@Test
	void test2() {
		Medico medico = new Medico("Dr. Ardel", "11031999", Especialidade.DERMATOLOGISTA);
		Sala sala = new Sala("Sala 2", TipoSala.SALA_GRANDE);

		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		LocalDateTime horaInicio = formater.parseLocalDateTime("2020-11-30 19:00");
		LocalDateTime horaFinal = formater.parseLocalDateTime("2020-11-30 22:00");

		Reserva reserva = new Reserva(medico, sala, horaInicio, horaFinal);
		assertEquals("Dermatologistas só podem reservar salas pequenas!", isReservaValid(reserva));

	}

	// Validação de Neurologista com sala pequena:
	@Test
	void test3() {
		Medico medico = new Medico("Dr. Ademar", "11112011", Especialidade.NEUROLOGISTA);
		Sala sala = new Sala("Sala 1", TipoSala.SALA_PEQUENA);

		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		LocalDateTime horaInicio = formater.parseLocalDateTime("2020-11-30 13:00");
		LocalDateTime horaFinal = formater.parseLocalDateTime("2020-11-30 19:00");

		Reserva reserva = new Reserva(medico, sala, horaInicio, horaFinal);
		assertEquals("Cirurgiões e neurologistas não podem usar salas pequenas", isReservaValid(reserva));
	}

	// Validação de uma reserva antes das 6 manhã:
	@Test
	void test4() {
		Medico medico = new Medico("Dr. Gabriel", "20102000", Especialidade.CIRURGIAO);
		Sala sala = new Sala("Sala 4", TipoSala.SALA_GRANDE);

		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		LocalDateTime horaInicio = formater.parseLocalDateTime("2020-11-30 05:00");
		LocalDateTime horaFinal = formater.parseLocalDateTime("2020-11-30 08:00");

		Reserva reserva = new Reserva(medico, sala, horaInicio, horaFinal);
		assertEquals("Reservas nao podem iniciar antes das 06:00", isReservaValid(reserva));
	}

	// Validação de uma reserva após as 22 da noite:
	@Test
	void test5() {
		Medico medico = new Medico("Dr. Felipe", "09021999", Especialidade.NEUROLOGISTA);
		Sala sala = new Sala("Sala 3", TipoSala.SALA_ALTO_RISCO);

		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		LocalDateTime horaInicio = formater.parseLocalDateTime("2020-11-30 18:00");
		LocalDateTime horaFinal = formater.parseLocalDateTime("2020-11-30 23:00");

		Reserva reserva = new Reserva(medico, sala, horaInicio, horaFinal);
		assertEquals("Reservas não podem terminar depois das 22:00", isReservaValid(reserva));
	}

	// Validação Sucesso ao reservar uma reserva:
	@Test
	void test6() {
		Medico medico = new Medico("Dr. Callegari", "54321", Especialidade.NEUROLOGISTA);
		Sala sala = new Sala("Sala 7", TipoSala.SALA_ALTO_RISCO);

		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		LocalDateTime horaInicio = formater.parseLocalDateTime("2020-11-30 13:00");
		LocalDateTime horaFinal = formater.parseLocalDateTime("2020-11-30 22:00");

		Reserva reserva = new Reserva(medico, sala, horaInicio, horaFinal);
		assertEquals("sucesso", isReservaValid(reserva));
	}

	// Validação Sucesso ao reservar uma reserva:
	@Test
	void test7() {
		Medico medico = new Medico("Dr. Joseph", "12345", Especialidade.CIRURGIAO);
		Sala sala = new Sala("Sala 3", TipoSala.SALA_GRANDE);

		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		LocalDateTime horaInicio = formater.parseLocalDateTime("2020-11-30 19:00");
		LocalDateTime horaFinal = formater.parseLocalDateTime("2020-11-30 22:00");

		Reserva reserva = new Reserva(medico, sala, horaInicio, horaFinal);
		assertEquals("sucesso", isReservaValid(reserva));
	}

	// Validação onde a hora final é menor que a hora inicial:
	@Test
	void test8() {
		Medico medico = new Medico("Dr. Ademar", "11112011", Especialidade.CIRURGIAO);
		Sala sala = new Sala("Sala 1", TipoSala.SALA_GRANDE);

		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		LocalDateTime horaInicio = formater.parseLocalDateTime("2020-11-30 22:00");
		LocalDateTime horaFinal = formater.parseLocalDateTime("2020-11-30 19:00");

		Reserva reserva = new Reserva(medico, sala, horaInicio, horaFinal);
		assertEquals("Fim da reserva não pode ser antes de seu inicio", isReservaValid(reserva));
	}

	// Validação de tempo minimo sala de alto risco:
	@Test
	void test9() {
		Medico medico = new Medico("Dr. Ademar", "11112011", Especialidade.CIRURGIAO);
		Sala sala = new Sala("Sala 10", TipoSala.SALA_ALTO_RISCO);

		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		LocalDateTime horaInicio = formater.parseLocalDateTime("2020-11-30 16:00");
		LocalDateTime horaFinal = formater.parseLocalDateTime("2020-11-30 18:00");

		Reserva reserva = new Reserva(medico, sala, horaInicio, horaFinal);
		assertEquals("Salas de alto risco tem um tempo minimo de reserva de 3 horas", isReservaValid(reserva));
	}

	// Validação de tempo mínimo de 2 horas:
	@Test
	void test10() {
		Medico medico = new Medico("Dr. Ardel", "11031999", Especialidade.DERMATOLOGISTA);
		Sala sala = new Sala("Sala 2", TipoSala.SALA_PEQUENA);

		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
		LocalDateTime horaInicio = formater.parseLocalDateTime("2020-11-30 14:00");
		LocalDateTime horaFinal = formater.parseLocalDateTime("2020-11-30 15:00");

		Reserva reserva = new Reserva(medico, sala, horaInicio, horaFinal);
		assertEquals("Reservas tem um tempo minimo de 2 horas", isReservaValid(reserva));
	}

}