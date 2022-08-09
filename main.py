import os
import funciones

#Crea carpeta principal
if not os.path.isdir('gherkins'):
    os.mkdir('gherkins')
    print("carpeta creda, i")
else:
    funciones.crearCSVdeArchivos("gherkins", "listaCustody")
    #Ejecuta la interfaz
    carpetaMidd, opcion_crear_jsons, opcion_crear_steps, opcion_crear_servicio, opcion_crear_test = funciones.interfaz()

    #Adapta los gherkins
    funciones.modificarListaDeArchivos("gherkins", 'Transacci�n', "Transaccion")
    funciones.modificarListaDeArchivos("gherkins", 'Transaccin', "Transaccion")
    funciones.modificarListaDeArchivos("gherkins", "operaci�n", "operacion")
    funciones.modificarListaDeArchivos("gherkins", "operacin", "operacion")

    #Crea los datos
    if opcion_crear_jsons == "1":
        listado_de_mocks = funciones.obtener_datos_y_o_crear_jsons()
    else:
        listado_de_mocks = funciones.obtener_datos_y_o_crear_jsons(False)
    if opcion_crear_steps == "1":
        funciones.crear_steps(listado_de_mocks, carpetaMidd)
    if opcion_crear_servicio == "1":
        funciones.crear_transaccion(listado_de_mocks, carpetaMidd)
    if opcion_crear_test == "1":
        funciones.crear_test(listado_de_mocks, carpetaMidd)
