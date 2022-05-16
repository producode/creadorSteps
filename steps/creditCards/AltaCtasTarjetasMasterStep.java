package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.AltaCtasTarjetasMasterInput;
import com.ebanking.midd.model.RequestAltaCtasTarjetasMaster;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseAltaCtasTarjetasMaster;
import com.ebanking.midd.service.CreditcardsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AltaCtasTarjetasMasterStep {
	private final CreditcardsImpl creditCards = new CreditcardsImpl();
	private final RequestAltaCtasTarjetasMaster request = new RequestAltaCtasTarjetasMaster();
	private final ResponseAltaCtasTarjetasMaster response = new ResponseAltaCtasTarjetasMaster();

	// Given Block BGN
	@Given("[alta_ctas_tarjetas_master] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[alta_ctas_tarjetas_master] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[alta_ctas_tarjetas_master] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		AltaCtasTarjetasMasterInput data = new AltaCtasTarjetasMasterInput();

		Map<String, String> parametros = parametria.get(0);
		data = (AltaCtasTarjetasMasterInput) TransactionUtil.initializeDataWithStringMap(AltaCtasTarjetasMasterInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[alta_ctas_tarjetas_master] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseAltaCtasTarjetasMaster rsp = creditCards.altaCtasTarjetasMaster(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[alta_ctas_tarjetas_master] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[alta_ctas_tarjetas_master] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[alta_ctas_tarjetas_master] segun el caso de uso {string}")
	public void checkCase(final String descripcion) {

	}
	// Then Block END
}
