package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.ConsultaClienteDepuradoInput;
import com.ebanking.midd.model.RequestConsultaClienteDepurado;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseConsultaClienteDepurado;
import com.ebanking.midd.service.ClientsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ConsultaClienteDepuradoStep {
	private final ClientsImpl clients = new ClientsImpl();
	private final RequestConsultaClienteDepurado request = new RequestConsultaClienteDepurado();
	private final ResponseConsultaClienteDepurado response = new ResponseConsultaClienteDepurado();

	// Given Block BGN
	@Given("[consulta_cliente_depurado] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[consulta_cliente_depurado] se usa el channel {string} para el encabezado de la operaci�n")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[consulta_cliente_depurado] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		ConsultaClienteDepuradoInput data = new ConsultaClienteDepuradoInput();

		Map<String, String> parametros = parametria.get(0);
		data = (ConsultaClienteDepuradoInput) TransactionUtil.initializeDataWithStringMap(ConsultaClienteDepuradoInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[consulta_cliente_depurado] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseConsultaClienteDepurado rsp = clients.consultaClienteDepurado(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[consulta_cliente_depurado] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[consulta_cliente_depurado] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[consulta_personas_especiales] segun el caso de uso {string}")
	public void checkCase() {

	}
	// Then Block END
}
