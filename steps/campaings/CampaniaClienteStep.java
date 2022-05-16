package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.CampaniaClienteInput;
import com.ebanking.midd.model.RequestCampaniaCliente;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseCampaniaCliente;
import com.ebanking.midd.service.CampaingsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CampaniaClienteStep {
	private final CampaingsImpl campaings = new CampaingsImpl();
	private final RequestCampaniaCliente request = new RequestCampaniaCliente();
	private final ResponseCampaniaCliente response = new ResponseCampaniaCliente();

	// Given Block BGN
	@Given("[campania_cliente] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[campania_cliente] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[campania_cliente] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		CampaniaClienteInput data = new CampaniaClienteInput();

		Map<String, String> parametros = parametria.get(0);
		data = (CampaniaClienteInput) TransactionUtil.initializeDataWithStringMap(CampaniaClienteInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[campania_cliente] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseCampaniaCliente rsp = campaings.campaniaCliente(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[campania_cliente] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[campania_cliente] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[campania_cliente] segun el caso de uso {string}")
	public void checkCase(final String descripcion) {

	}
	// Then Block END
}
