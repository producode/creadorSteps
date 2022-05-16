# This is a sample Python script.

# Press Mayús+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

def decapitalize_first_letter(s, upper_rest = False):
  return ''.join([s[:1].lower(), (s[1:].upper() if upper_rest else s[1:])])

MIDD = "campaings"

mock_utils_asign = [
    {"programIntern": "BUCA", "programFormat1": "campania_cliente"},
    {"programIntern": "BUCK", "programFormat1": "consulta_cliente"},
    {"programIntern": "BUBC", "programFormat1": "consulta_ampliada_cliente"}
                ]
programas_nombres = [elemento["programFormat1"] for elemento in mock_utils_asign]
programasNombres = [(''.join([word.capitalize() for word in elemento["programFormat1"].split("_")])) for elemento in mock_utils_asign]

for i in range(len(programasNombres)):
    f = open('base.txt', 'r')
    programaNuevo = f.read()
    programaNuevo = programaNuevo.replace("XXXX", programasNombres[i])
    programaNuevo = programaNuevo.replace("YYYY", programas_nombres[i])
    programaNuevo = programaNuevo.replace("AAAA", MIDD)
    programaNuevo = programaNuevo.replace("BBBB", MIDD.capitalize())
    programaNuevo = programaNuevo.replace("CCCC", decapitalize_first_letter(programasNombres[i]))
    nf = open('steps/{midd}/{nombre}Step.java'.format(nombre=programasNombres[i], midd=MIDD), 'w')
    nf.write(programaNuevo)
    nf.close()
    f.close()
