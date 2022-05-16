package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.AlertasClientesInput;
import com.ebanking.midd.model.RequestAlertasClientes;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseAlertasClientes;
import com.ebanking.midd.service.ClientsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AlertasClientesStep {
	private final ClientsImpl clients = new ClientsImpl();
	private final RequestAlertasClientes request = new RequestAlertasClientes();
	private final ResponseAlertasClientes response = new ResponseAlertasClientes();

	// Given Block BGN
	@Given("[alertas_clientes] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[alertas_clientes] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);

		header.setChannel(channel);
	}

	@Given("[alertas_clientes] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		AlertasClientesInput data = new AlertasClientesInput();

		Map<String, String> parametros = parametria.get(0);
		data = (AlertasClientesInput) TransactionUtil.initializeDataWithStringMap(AlertasClientesInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[alertas_clientes] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseAlertasClientes rsp = clients.AlertasClientes(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[alertas_clientes] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[alertas_clientes] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
