package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.MasterConsumosCuotasInput;
import com.ebanking.midd.model.RequestMasterConsumosCuotas;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseMasterConsumosCuotas;
import com.ebanking.midd.service.CreditcardsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MasterConsumosCuotasStep {
	private final CreditcardsImpl creditCards = new CreditcardsImpl();
	private final RequestMasterConsumosCuotas request = new RequestMasterConsumosCuotas();
	private final ResponseMasterConsumosCuotas response = new ResponseMasterConsumosCuotas();

	// Given Block BGN
	@Given("[master_consumos_cuotas] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[master_consumos_cuotas] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[master_consumos_cuotas] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		MasterConsumosCuotasInput data = new MasterConsumosCuotasInput();

		Map<String, String> parametros = parametria.get(0);
		data = (MasterConsumosCuotasInput) TransactionUtil.initializeDataWithStringMap(MasterConsumosCuotasInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[master_consumos_cuotas] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseMasterConsumosCuotas rsp = creditCards.masterConsumosCuotas(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[master_consumos_cuotas] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[master_consumos_cuotas] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[master_consumos_cuotas] segun el caso de uso {string}")
	public void checkCase(final String descripcion) {

	}
	// Then Block END
}
