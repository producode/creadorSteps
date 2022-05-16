package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.VisaValidacionCvv2Input;
import com.ebanking.midd.model.RequestVisaValidacionCvv2;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseVisaValidacionCvv2;
import com.ebanking.midd.service.CreditcardsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class VisaValidacionCvv2Step {
	private final CreditcardsImpl creditCards = new CreditcardsImpl();
	private final RequestVisaValidacionCvv2 request = new RequestVisaValidacionCvv2();
	private final ResponseVisaValidacionCvv2 response = new ResponseVisaValidacionCvv2();

	// Given Block BGN
	@Given("[visa_validacion_cvv2] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[visa_validacion_cvv2] se usa el channel {string} para el encabezado de la operaci�n")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[visa_validacion_cvv2] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		VisaValidacionCvv2Input data = new VisaValidacionCvv2Input();

		Map<String, String> parametros = parametria.get(0);
		data = (VisaValidacionCvv2Input) TransactionUtil.initializeDataWithStringMap(VisaValidacionCvv2Input.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[visa_validacion_cvv2] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseVisaValidacionCvv2 rsp = creditCards.visaValidacionCvv2(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[visa_validacion_cvv2] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[visa_validacion_cvv2] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[consulta_personas_especiales] segun el caso de uso {string}")
	public void checkCase() {

	}
	// Then Block END
}
