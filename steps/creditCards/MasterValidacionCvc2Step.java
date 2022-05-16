package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.MasterValidacionCvc2Input;
import com.ebanking.midd.model.RequestMasterValidacionCvc2;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseMasterValidacionCvc2;
import com.ebanking.midd.service.CreditcardsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MasterValidacionCvc2Step {
	private final CreditcardsImpl creditCards = new CreditcardsImpl();
	private final RequestMasterValidacionCvc2 request = new RequestMasterValidacionCvc2();
	private final ResponseMasterValidacionCvc2 response = new ResponseMasterValidacionCvc2();

	// Given Block BGN
	@Given("[master_validacion_cvc2] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[master_validacion_cvc2] se usa el channel {string} para el encabezado de la operaci�n")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[master_validacion_cvc2] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		MasterValidacionCvc2Input data = new MasterValidacionCvc2Input();

		Map<String, String> parametros = parametria.get(0);
		data = (MasterValidacionCvc2Input) TransactionUtil.initializeDataWithStringMap(MasterValidacionCvc2Input.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[master_validacion_cvc2] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseMasterValidacionCvc2 rsp = creditCards.masterValidacionCvc2(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[master_validacion_cvc2] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[master_validacion_cvc2] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[consulta_personas_especiales] segun el caso de uso {string}")
	public void checkCase() {

	}
	// Then Block END
}
