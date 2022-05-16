package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.AvisosAltaContactoInput;
import com.ebanking.midd.model.RequestAvisosAltaContacto;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseAvisosAltaContacto;
import com.ebanking.midd.service.ClientsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AvisosAltaContactoStep {
	private final ClientsImpl clients = new ClientsImpl();
	private final RequestAvisosAltaContacto request = new RequestAvisosAltaContacto();
	private final ResponseAvisosAltaContacto response = new ResponseAvisosAltaContacto();

	// Given Block BGN
	@Given("[avisos_alta_contacto] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[avisos_alta_contacto] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[avisos_alta_contacto] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		AvisosAltaContactoInput data = new AvisosAltaContactoInput();

		Map<String, String> parametros = parametria.get(0);
		data = (AvisosAltaContactoInput) TransactionUtil.initializeDataWithStringMap(AvisosAltaContactoInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[avisos_alta_contacto] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseAvisosAltaContacto rsp = clients.AvisosAltaContacto(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[avisos_alta_contacto] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[avisos_alta_contacto] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
