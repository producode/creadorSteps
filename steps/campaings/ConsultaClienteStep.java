package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.ConsultaClienteInput;
import com.ebanking.midd.model.RequestConsultaCliente;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseConsultaCliente;
import com.ebanking.midd.service.CampaingsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ConsultaClienteStep {
	private final CampaingsImpl campaings = new CampaingsImpl();
	private final RequestConsultaCliente request = new RequestConsultaCliente();
	private final ResponseConsultaCliente response = new ResponseConsultaCliente();

	// Given Block BGN
	@Given("[consulta_cliente] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[consulta_cliente] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[consulta_cliente] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		ConsultaClienteInput data = new ConsultaClienteInput();

		Map<String, String> parametros = parametria.get(0);
		data = (ConsultaClienteInput) TransactionUtil.initializeDataWithStringMap(ConsultaClienteInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[consulta_cliente] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseConsultaCliente rsp = campaings.consultaCliente(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[consulta_cliente] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[consulta_cliente] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[consulta_cliente] segun el caso de uso {string}")
	public void checkCase(final String descripcion) {

	}
	// Then Block END
}
