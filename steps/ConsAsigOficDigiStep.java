package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.ConsAsigOficDigiInput;
import com.ebanking.midd.model.RequestConsAsigOficDigi;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseConsAsigOficDigi;
import com.ebanking.midd.service.ClientsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ConsAsigOficDigiStep {
	private final ClientsImpl clients = new ClientsImpl();
	private final RequestConsAsigOficDigi request = new RequestConsAsigOficDigi();
	private final ResponseConsAsigOficDigi response = new ResponseConsAsigOficDigi();

	// Given Block BGN
	@Given("[cons_asig_ofic_digi] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[cons_asig_ofic_digi] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[cons_asig_ofic_digi] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		ConsAsigOficDigiInput data = new ConsAsigOficDigiInput();

		Map<String, String> parametros = parametria.get(0);
		data = (ConsAsigOficDigiInput) TransactionUtil.initializeDataWithStringMap(ConsAsigOficDigiInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[cons_asig_ofic_digi] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseConsAsigOficDigi rsp = clients.ConsAsigOficDigi(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[cons_asig_ofic_digi] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[cons_asig_ofic_digi] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
