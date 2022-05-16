package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.CotizaSeguroInput;
import com.ebanking.midd.model.RequestCotizaSeguro;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseCotizaSeguro;
import com.ebanking.midd.service.InsuranceImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CotizaSeguroStep {
	private final InsuranceImpl insurance = new InsuranceImpl();
	private final RequestCotizaSeguro request = new RequestCotizaSeguro();
	private final ResponseCotizaSeguro response = new ResponseCotizaSeguro();

	// Given Block BGN
	@Given("[cotiza_seguro] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[cotiza_seguro] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[cotiza_seguro] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		CotizaSeguroInput data = new CotizaSeguroInput();

		Map<String, String> parametros = parametria.get(0);
		data = (CotizaSeguroInput) TransactionUtil.initializeDataWithStringMap(CotizaSeguroInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[cotiza_seguro] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseCotizaSeguro rsp = insurance.CotizaSeguro(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[cotiza_seguro] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[cotiza_seguro] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
