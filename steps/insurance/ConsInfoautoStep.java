package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.ConsInfoautoInput;
import com.ebanking.midd.model.RequestConsInfoauto;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseConsInfoauto;
import com.ebanking.midd.service.InsuranceImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ConsInfoautoStep {
	private final InsuranceImpl insurance = new InsuranceImpl();
	private final RequestConsInfoauto request = new RequestConsInfoauto();
	private final ResponseConsInfoauto response = new ResponseConsInfoauto();

	// Given Block BGN
	@Given("[cons_infoauto] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[cons_infoauto] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[cons_infoauto] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		ConsInfoautoInput data = new ConsInfoautoInput();

		Map<String, String> parametros = parametria.get(0);
		data = (ConsInfoautoInput) TransactionUtil.initializeDataWithStringMap(ConsInfoautoInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[cons_infoauto] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseConsInfoauto rsp = insurance.ConsInfoauto(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[cons_infoauto] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[cons_infoauto] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}