package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.MasterConsDisponibleInput;
import com.ebanking.midd.model.RequestMasterConsDisponible;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseMasterConsDisponible;
import com.ebanking.midd.service.CreditcardsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MasterConsDisponibleStep {
	private final CreditcardsImpl creditCards = new CreditcardsImpl();
	private final RequestMasterConsDisponible request = new RequestMasterConsDisponible();
	private final ResponseMasterConsDisponible response = new ResponseMasterConsDisponible();

	// Given Block BGN
	@Given("[master_cons_disponible] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[master_cons_disponible] se usa el channel {string} para el encabezado de la operaci�n")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[master_cons_disponible] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		MasterConsDisponibleInput data = new MasterConsDisponibleInput();

		Map<String, String> parametros = parametria.get(0);
		data = (MasterConsDisponibleInput) TransactionUtil.initializeDataWithStringMap(MasterConsDisponibleInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[master_cons_disponible] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseMasterConsDisponible rsp = creditCards.masterConsDisponible(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[master_cons_disponible] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[master_cons_disponible] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[consulta_personas_especiales] segun el caso de uso {string}")
	public void checkCase() {

	}
	// Then Block END
}
