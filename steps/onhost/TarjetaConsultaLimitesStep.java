package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.TarjetaConsultaLimitesInput;
import com.ebanking.midd.model.RequestTarjetaConsultaLimites;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseTarjetaConsultaLimites;
import com.ebanking.midd.service.OnhostImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TarjetaConsultaLimitesStep {
	private final OnhostImpl onhost = new OnhostImpl();
	private final RequestTarjetaConsultaLimites request = new RequestTarjetaConsultaLimites();
	private final ResponseTarjetaConsultaLimites response = new ResponseTarjetaConsultaLimites();

	// Given Block BGN
	@Given("[tarjeta_consulta_limites] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[tarjeta_consulta_limites] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[tarjeta_consulta_limites] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		TarjetaConsultaLimitesInput data = new TarjetaConsultaLimitesInput();

		Map<String, String> parametros = parametria.get(0);
		data = (TarjetaConsultaLimitesInput) TransactionUtil.initializeDataWithStringMap(TarjetaConsultaLimitesInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[tarjeta_consulta_limites] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseTarjetaConsultaLimites rsp = onhost.tarjetaConsultaLimites(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[tarjeta_consulta_limites] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[tarjeta_consulta_limites] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[tarjeta_consulta_limites] segun el caso de uso {string}")
	public void checkCase(final String descripcion) {

	}
	// Then Block END
}
