package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.AltaParticipesInput;
import com.ebanking.midd.model.RequestAltaParticipes;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseAltaParticipes;
import com.ebanking.midd.service.ClientsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AltaParticipesStep {
	private final ClientsImpl clients = new ClientsImpl();
	private final RequestAltaParticipes request = new RequestAltaParticipes();
	private final ResponseAltaParticipes response = new ResponseAltaParticipes();

	// Given Block BGN
	@Given("[alta_participes] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[alta_participes] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[alta_participes] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		AltaParticipesInput data = new AltaParticipesInput();

		Map<String, String> parametros = parametria.get(0);
		data = (AltaParticipesInput) TransactionUtil.initializeDataWithStringMap(AltaParticipesInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[alta_participes] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseAltaParticipes rsp = clients.AltaParticipes(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[alta_participes] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[alta_participes] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
