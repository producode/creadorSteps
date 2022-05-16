package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.HabilitacionTarjetasInput;
import com.ebanking.midd.model.RequestHabilitacionTarjetas;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseHabilitacionTarjetas;
import com.ebanking.midd.service.OnhostImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HabilitacionTarjetasStep {
	private final OnhostImpl onhost = new OnhostImpl();
	private final RequestHabilitacionTarjetas request = new RequestHabilitacionTarjetas();
	private final ResponseHabilitacionTarjetas response = new ResponseHabilitacionTarjetas();

	// Given Block BGN
	@Given("[habilitacion_tarjetas] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[habilitacion_tarjetas] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[habilitacion_tarjetas] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		HabilitacionTarjetasInput data = new HabilitacionTarjetasInput();

		Map<String, String> parametros = parametria.get(0);
		data = (HabilitacionTarjetasInput) TransactionUtil.initializeDataWithStringMap(HabilitacionTarjetasInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[habilitacion_tarjetas] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseHabilitacionTarjetas rsp = onhost.habilitacionTarjetas(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[habilitacion_tarjetas] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[habilitacion_tarjetas] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[habilitacion_tarjetas] segun el caso de uso {string}")
	public void checkCase(final String descripcion) {

	}
	// Then Block END
}
