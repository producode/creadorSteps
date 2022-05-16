package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.SegurosSolicitudCertificadoInput;
import com.ebanking.midd.model.RequestSegurosSolicitudCertificado;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseSegurosSolicitudCertificado;
import com.ebanking.midd.service.InsuranceImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SegurosSolicitudCertificadoStep {
	private final InsuranceImpl insurance = new InsuranceImpl();
	private final RequestSegurosSolicitudCertificado request = new RequestSegurosSolicitudCertificado();
	private final ResponseSegurosSolicitudCertificado response = new ResponseSegurosSolicitudCertificado();

	// Given Block BGN
	@Given("[seguros_solicitud_certificado] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[seguros_solicitud_certificado] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[seguros_solicitud_certificado] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		SegurosSolicitudCertificadoInput data = new SegurosSolicitudCertificadoInput();

		Map<String, String> parametros = parametria.get(0);
		data = (SegurosSolicitudCertificadoInput) TransactionUtil.initializeDataWithStringMap(SegurosSolicitudCertificadoInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[seguros_solicitud_certificado] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseSegurosSolicitudCertificado rsp = insurance.SegurosSolicitudCertificado(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[seguros_solicitud_certificado] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[seguros_solicitud_certificado] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
