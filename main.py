import os
import funciones

#Crea carpeta principal
if not os.path.isdir('gherkins'):
    os.mkdir('gherkins')
    print("carpeta creda, i")
else:
    #Ejecuta la interfaz
    carpetaMidd, opcion_crear_jsons, opcion_crear_steps = funciones.interfaz()

    #Adapta los gherkins
    funciones.modificarListaDeArchivos("gherkins", 'Transacci�n', "Transaccion")
    funciones.modificarListaDeArchivos("gherkins", 'Transaccin', "Transaccion")

    #Crea los datos
    if opcion_crear_jsons == "1":
        listado_de_mocks = funciones.obtener_datos_y_o_crear_jsons()
    else:
        listado_de_mocks = funciones.obtener_datos_y_o_crear_jsons(False)
    if opcion_crear_steps == "1":
        funciones.crear_steps(listado_de_mocks, carpetaMidd)
