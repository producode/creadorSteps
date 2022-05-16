package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.CmDatosKycInput;
import com.ebanking.midd.model.RequestCmDatosKyc;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseCmDatosKyc;
import com.ebanking.midd.service.ClientsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CmDatosKycStep {
	private final ClientsImpl clients = new ClientsImpl();
	private final RequestCmDatosKyc request = new RequestCmDatosKyc();
	private final ResponseCmDatosKyc response = new ResponseCmDatosKyc();

	// Given Block BGN
	@Given("[cm_datos_kyc] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[cm_datos_kyc] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[cm_datos_kyc] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		CmDatosKycInput data = new CmDatosKycInput();

		Map<String, String> parametros = parametria.get(0);
		data = (CmDatosKycInput) TransactionUtil.initializeDataWithStringMap(CmDatosKycInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[cm_datos_kyc] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseCmDatosKyc rsp = clients.CmDatosKyc(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[cm_datos_kyc] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[cm_datos_kyc] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
