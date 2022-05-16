package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.ConsGralTarjetasInput;
import com.ebanking.midd.model.RequestConsGralTarjetas;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseConsGralTarjetas;
import com.ebanking.midd.service.CreditcardsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ConsGralTarjetasStep {
	private final CreditcardsImpl creditCards = new CreditcardsImpl();
	private final RequestConsGralTarjetas request = new RequestConsGralTarjetas();
	private final ResponseConsGralTarjetas response = new ResponseConsGralTarjetas();

	// Given Block BGN
	@Given("[cons_gral_tarjetas] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[cons_gral_tarjetas] se usa el channel {string} para el encabezado de la operaci�n")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[cons_gral_tarjetas] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		ConsGralTarjetasInput data = new ConsGralTarjetasInput();

		Map<String, String> parametros = parametria.get(0);
		data = (ConsGralTarjetasInput) TransactionUtil.initializeDataWithStringMap(ConsGralTarjetasInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[cons_gral_tarjetas] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseConsGralTarjetas rsp = creditCards.consGralTarjetas(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[cons_gral_tarjetas] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[cons_gral_tarjetas] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[consulta_personas_especiales] segun el caso de uso {string}")
	public void checkCase() {

	}
	// Then Block END
}
