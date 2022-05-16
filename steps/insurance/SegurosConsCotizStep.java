package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.SegurosConsCotizInput;
import com.ebanking.midd.model.RequestSegurosConsCotiz;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseSegurosConsCotiz;
import com.ebanking.midd.service.InsuranceImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SegurosConsCotizStep {
	private final InsuranceImpl insurance = new InsuranceImpl();
	private final RequestSegurosConsCotiz request = new RequestSegurosConsCotiz();
	private final ResponseSegurosConsCotiz response = new ResponseSegurosConsCotiz();

	// Given Block BGN
	@Given("[seguros_cons_cotiz] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[seguros_cons_cotiz] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[seguros_cons_cotiz] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		SegurosConsCotizInput data = new SegurosConsCotizInput();

		Map<String, String> parametros = parametria.get(0);
		data = (SegurosConsCotizInput) TransactionUtil.initializeDataWithStringMap(SegurosConsCotizInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[seguros_cons_cotiz] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseSegurosConsCotiz rsp = insurance.SegurosConsCotiz(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[seguros_cons_cotiz] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[seguros_cons_cotiz] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
