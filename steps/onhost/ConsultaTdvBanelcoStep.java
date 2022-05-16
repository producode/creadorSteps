package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.ConsultaTdvBanelcoInput;
import com.ebanking.midd.model.RequestConsultaTdvBanelco;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseConsultaTdvBanelco;
import com.ebanking.midd.service.OnhostImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ConsultaTdvBanelcoStep {
	private final OnhostImpl onhost = new OnhostImpl();
	private final RequestConsultaTdvBanelco request = new RequestConsultaTdvBanelco();
	private final ResponseConsultaTdvBanelco response = new ResponseConsultaTdvBanelco();

	// Given Block BGN
	@Given("[consulta_tdv_banelco] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[consulta_tdv_banelco] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[consulta_tdv_banelco] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		ConsultaTdvBanelcoInput data = new ConsultaTdvBanelcoInput();

		Map<String, String> parametros = parametria.get(0);
		data = (ConsultaTdvBanelcoInput) TransactionUtil.initializeDataWithStringMap(ConsultaTdvBanelcoInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[consulta_tdv_banelco] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseConsultaTdvBanelco rsp = onhost.consultaTdvBanelco(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[consulta_tdv_banelco] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[consulta_tdv_banelco] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[consulta_tdv_banelco] segun el caso de uso {string}")
	public void checkCase(final String descripcion) {

	}
	// Then Block END
}
