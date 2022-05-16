package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.SegurosDetalleCoberturasInput;
import com.ebanking.midd.model.RequestSegurosDetalleCoberturas;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseSegurosDetalleCoberturas;
import com.ebanking.midd.service.InsuranceImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SegurosDetalleCoberturasStep {
	private final InsuranceImpl insurance = new InsuranceImpl();
	private final RequestSegurosDetalleCoberturas request = new RequestSegurosDetalleCoberturas();
	private final ResponseSegurosDetalleCoberturas response = new ResponseSegurosDetalleCoberturas();

	// Given Block BGN
	@Given("[seguros_detalle_coberturas] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[seguros_detalle_coberturas] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[seguros_detalle_coberturas] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		SegurosDetalleCoberturasInput data = new SegurosDetalleCoberturasInput();

		Map<String, String> parametros = parametria.get(0);
		data = (SegurosDetalleCoberturasInput) TransactionUtil.initializeDataWithStringMap(SegurosDetalleCoberturasInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[seguros_detalle_coberturas] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseSegurosDetalleCoberturas rsp = insurance.SegurosDetalleCoberturas(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[seguros_detalle_coberturas] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		//assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[seguros_detalle_coberturas] no hay codigo de error")
	public void checkErrorIsEmpty() {
		//assertNull(response.getHeader().getError());
	}
	// Then Block END
}
