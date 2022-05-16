package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.SegurosPolizasConsPuntualInput;
import com.ebanking.midd.model.RequestSegurosPolizasConsPuntual;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseSegurosPolizasConsPuntual;
import com.ebanking.midd.service.InsuranceImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SegurosPolizasConsPuntualStep {
	private final InsuranceImpl insurance = new InsuranceImpl();
	private final RequestSegurosPolizasConsPuntual request = new RequestSegurosPolizasConsPuntual();
	private final ResponseSegurosPolizasConsPuntual response = new ResponseSegurosPolizasConsPuntual();

	// Given Block BGN
	@Given("[seguros_polizas_cons_puntual] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[seguros_polizas_cons_puntual] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[seguros_polizas_cons_puntual] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		SegurosPolizasConsPuntualInput data = new SegurosPolizasConsPuntualInput();

		Map<String, String> parametros = parametria.get(0);
		data = (SegurosPolizasConsPuntualInput) TransactionUtil.initializeDataWithStringMap(SegurosPolizasConsPuntualInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[seguros_polizas_cons_puntual] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseSegurosPolizasConsPuntual rsp = insurance.SegurosPolizasConsPuntual(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[seguros_polizas_cons_puntual] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[seguros_polizas_cons_puntual] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
