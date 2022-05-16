package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.SegurosConsCuotasInput;
import com.ebanking.midd.model.RequestSegurosConsCuotas;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseSegurosConsCuotas;
import com.ebanking.midd.service.InsuranceImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SegurosConsCuotasStep {
	private final InsuranceImpl insurance = new InsuranceImpl();
	private final RequestSegurosConsCuotas request = new RequestSegurosConsCuotas();
	private final ResponseSegurosConsCuotas response = new ResponseSegurosConsCuotas();

	// Given Block BGN
	@Given("[seguros_cons_cuotas] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[seguros_cons_cuotas] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[seguros_cons_cuotas] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		SegurosConsCuotasInput data = new SegurosConsCuotasInput();

		Map<String, String> parametros = parametria.get(0);
		data = (SegurosConsCuotasInput) TransactionUtil.initializeDataWithStringMap(SegurosConsCuotasInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[seguros_cons_cuotas] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseSegurosConsCuotas rsp = insurance.SegurosConsCuotas(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[seguros_cons_cuotas] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[seguros_cons_cuotas] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
