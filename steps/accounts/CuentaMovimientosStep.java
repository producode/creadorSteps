package com.ebanking.midd.service.test.cucumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import com.ebanking.midd.model.CuentaMovimientosInput;
import com.ebanking.midd.model.RequestCuentaMovimientos;
import com.ebanking.midd.model.RequestHeader;
import com.ebanking.midd.model.ResponseCuentaMovimientos;
import com.ebanking.midd.service.AccountsImpl;
import com.ebanking.midd.service.test.mock.Common;
import com.ebanking.midd.util.TransactionUtil;
import com.midd.HttpConnector;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CuentaMovimientosStep {
	private final AccountsImpl accounts = new AccountsImpl();
	private final RequestCuentaMovimientos request = new RequestCuentaMovimientos();
	private final ResponseCuentaMovimientos response = new ResponseCuentaMovimientos();

	// Given Block BGN
	@Given("[cuenta_movimientos] se inicializan los servicios necesarios para realizar la prueba")
	public void init() {
		Common.startMockServer();
	}

	@Given("[cuenta_movimientos] se usa el channel {string} para el encabezado de la operacion")
	public void setHeader(String channel) {
		RequestHeader header = new RequestHeader();
		request.setHeader(header);
		header.setChannel(channel);
	}

	@Given("[cuenta_movimientos] usando los siguientes parametros")
	public void setDataValues(List<Map<String, String>> parametria) {
		CuentaMovimientosInput data = new CuentaMovimientosInput();

		Map<String, String> parametros = parametria.get(0);
		data = (CuentaMovimientosInput) TransactionUtil.initializeDataWithStringMap(CuentaMovimientosInput.class, parametros);
		request.setData(data);

		String parametrosAsString = parametros.keySet().stream().map(key -> key + ": " + parametros.get(key))
				.collect(Collectors.joining(" - ", "[ ", " ]"));

		System.out.println("parametros " + parametrosAsString);
	}
	// Given Block END

	// When Block BGN
	@When("[cuenta_movimientos] se realiza el llamado a ALTAMIRA")
	public void callAltamira() throws JAXBException {
		System.out.println(HttpConnector.marshal(request));
		ResponseCuentaMovimientos rsp = accounts.cuentaMovimientos(request);
		response.setHeader(rsp.getHeader());
		response.setData(rsp.getData());

		System.out.println(HttpConnector.marshal(request));
		System.out.println(HttpConnector.marshal(response));
	}
	// When Block END

	// Then Block BGN
	@Then("[cuenta_movimientos] se obtiene el resultado {string}")
	public void resultCode(final String resultado) {
		assertTrue(resultado.trim().equals(response.getHeader().getResultCode().trim()));
	}

	@Then("[cuenta_movimientos] no hay codigo de error")
	public void checkErrorIsEmpty() {
		assertNull(response.getHeader().getError());
	}

	@Then("[cuenta_movimientos] segun el caso de uso {string}")
	public void checkCase(final String descripcion) {

	}
	// Then Block END
}
