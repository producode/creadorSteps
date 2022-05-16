package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.SolicCotizSeguroInput;
import com.ebanking.midd.model.RequestSolicCotizSeguro;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseSolicCotizSeguro;
import com.ebanking.midd.service.InsuranceImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SolicCotizSeguroStep {
	private final InsuranceImpl insurance = new InsuranceImpl();
	private final RequestSolicCotizSeguro request = new RequestSolicCotizSeguro();
	private final ResponseSolicCotizSeguro response = new ResponseSolicCotizSeguro();

	// Given Block BGN
	@Given("[solic_cotiz_seguro] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[solic_cotiz_seguro] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[solic_cotiz_seguro] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		SolicCotizSeguroInput data = new SolicCotizSeguroInput();

		Map<String, String> parametros = parametria.get(0);
		data = (SolicCotizSeguroInput) TransactionUtil.initializeDataWithStringMap(SolicCotizSeguroInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[solic_cotiz_seguro] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseSolicCotizSeguro rsp = insurance.SolicCotizSeguro(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[solic_cotiz_seguro] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[solic_cotiz_seguro] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
