package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.ConsMovimientoInput;
import com.ebanking.midd.model.RequestConsMovimiento;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseConsMovimiento;
import com.ebanking.midd.service.AccountsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ConsMovimientoStep {
	private final AccountsImpl accounts = new AccountsImpl();
	private final RequestConsMovimiento request = new RequestConsMovimiento();
	private final ResponseConsMovimiento response = new ResponseConsMovimiento();

	// Given Block BGN
	@Given("[cons_movimiento] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[cons_movimiento] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[cons_movimiento] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		ConsMovimientoInput data = new ConsMovimientoInput();

		Map<String, String> parametros = parametria.get(0);
		data = (ConsMovimientoInput) TransactionUtil.initializeDataWithStringMap(ConsMovimientoInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[cons_movimiento] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseConsMovimiento rsp = accounts.consMovimiento(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[cons_movimiento] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[cons_movimiento] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[cons_movimiento] segun el caso de uso {string}")
	public void checkCase(final String descripcion) {

	}
	// Then Block END
}
