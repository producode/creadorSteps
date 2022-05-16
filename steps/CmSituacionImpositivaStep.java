package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.CmSituacionImpositivaInput;
import com.ebanking.midd.model.RequestCmSituacionImpositiva;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseCmSituacionImpositiva;
import com.ebanking.midd.service.ClientsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CmSituacionImpositivaStep {
	private final ClientsImpl clients = new ClientsImpl();
	private final RequestCmSituacionImpositiva request = new RequestCmSituacionImpositiva();
	private final ResponseCmSituacionImpositiva response = new ResponseCmSituacionImpositiva();

	// Given Block BGN
	@Given("[cm_situacion_impositiva] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[cm_situacion_impositiva] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[cm_situacion_impositiva] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		CmSituacionImpositivaInput data = new CmSituacionImpositivaInput();

		Map<String, String> parametros = parametria.get(0);
		data = (CmSituacionImpositivaInput) TransactionUtil.initializeDataWithStringMap(CmSituacionImpositivaInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[cm_situacion_impositiva] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseCmSituacionImpositiva rsp = clients.CmSituacionImpositiva(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[cm_situacion_impositiva] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[cm_situacion_impositiva] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
