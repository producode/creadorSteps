package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.VisaConsLiquidacionInput;
import com.ebanking.midd.model.RequestVisaConsLiquidacion;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseVisaConsLiquidacion;
import com.ebanking.midd.service.CreditcardsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class VisaConsLiquidacionStep {
	private final CreditcardsImpl creditCards = new CreditcardsImpl();
	private final RequestVisaConsLiquidacion request = new RequestVisaConsLiquidacion();
	private final ResponseVisaConsLiquidacion response = new ResponseVisaConsLiquidacion();

	// Given Block BGN
	@Given("[visa_cons_liquidacion] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[visa_cons_liquidacion] se usa el channel {string} para el encabezado de la operaci�n")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[visa_cons_liquidacion] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		VisaConsLiquidacionInput data = new VisaConsLiquidacionInput();

		Map<String, String> parametros = parametria.get(0);
		data = (VisaConsLiquidacionInput) TransactionUtil.initializeDataWithStringMap(VisaConsLiquidacionInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[visa_cons_liquidacion] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseVisaConsLiquidacion rsp = creditCards.visaConsLiquidacion(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[visa_cons_liquidacion] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[visa_cons_liquidacion] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[consulta_personas_especiales] segun el caso de uso {string}")
	public void checkCase() {

	}
	// Then Block END
}
