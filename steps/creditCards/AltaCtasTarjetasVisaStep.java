package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.AltaCtasTarjetasVisaInput;
import com.ebanking.midd.model.RequestAltaCtasTarjetasVisa;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseAltaCtasTarjetasVisa;
import com.ebanking.midd.service.CreditcardsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AltaCtasTarjetasVisaStep {
	private final CreditcardsImpl creditCards = new CreditcardsImpl();
	private final RequestAltaCtasTarjetasVisa request = new RequestAltaCtasTarjetasVisa();
	private final ResponseAltaCtasTarjetasVisa response = new ResponseAltaCtasTarjetasVisa();

	// Given Block BGN
	@Given("[alta_ctas_tarjetas_visa] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[alta_ctas_tarjetas_visa] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[alta_ctas_tarjetas_visa] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		AltaCtasTarjetasVisaInput data = new AltaCtasTarjetasVisaInput();

		Map<String, String> parametros = parametria.get(0);
		data = (AltaCtasTarjetasVisaInput) TransactionUtil.initializeDataWithStringMap(AltaCtasTarjetasVisaInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[alta_ctas_tarjetas_visa] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseAltaCtasTarjetasVisa rsp = creditCards.altaCtasTarjetasVisa(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[alta_ctas_tarjetas_visa] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[alta_ctas_tarjetas_visa] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[alta_ctas_tarjetas_visa] segun el caso de uso {string}")
	public void checkCase(final String descripcion) {

	}
	// Then Block END
}
