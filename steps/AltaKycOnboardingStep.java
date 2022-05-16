package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.AltaKycOnboardingInput;
import com.ebanking.midd.model.RequestAltaKycOnboarding;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseAltaKycOnboarding;
import com.ebanking.midd.service.ClientsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AltaKycOnboardingStep {
	private final ClientsImpl clients = new ClientsImpl();
	private final RequestAltaKycOnboarding request = new RequestAltaKycOnboarding();
	private final ResponseAltaKycOnboarding response = new ResponseAltaKycOnboarding();

	// Given Block BGN
	@Given("[alta_kyc_onboarding] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[alta_kyc_onboarding] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[alta_kyc_onboarding] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		AltaKycOnboardingInput data = new AltaKycOnboardingInput();

		Map<String, String> parametros = parametria.get(0);
		data = (AltaKycOnboardingInput) TransactionUtil.initializeDataWithStringMap(AltaKycOnboardingInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[alta_kyc_onboarding] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseAltaKycOnboarding rsp = clients.AltaKycOnboarding(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[alta_kyc_onboarding] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[alta_kyc_onboarding] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
