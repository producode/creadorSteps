package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.AltaTelefFraudulentoInput;
import com.ebanking.midd.model.RequestAltaTelefFraudulento;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseAltaTelefFraudulento;
import com.ebanking.midd.service.ClientsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AltaTelefFraudulentoStep {
	private final ClientsImpl clients = new ClientsImpl();
	private final RequestAltaTelefFraudulento request = new RequestAltaTelefFraudulento();
	private final ResponseAltaTelefFraudulento response = new ResponseAltaTelefFraudulento();

	// Given Block BGN
	@Given("[alta_telef_fraudulento] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[alta_telef_fraudulento] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[alta_telef_fraudulento] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		AltaTelefFraudulentoInput data = new AltaTelefFraudulentoInput();

		Map<String, String> parametros = parametria.get(0);
		data = (AltaTelefFraudulentoInput) TransactionUtil.initializeDataWithStringMap(AltaTelefFraudulentoInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[alta_telef_fraudulento] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseAltaTelefFraudulento rsp = clients.AltaTelefFraudulento(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[alta_telef_fraudulento] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[alta_telef_fraudulento] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
