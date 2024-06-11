package bryan.miranda.ejemplospinner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.dataClassDoctores

class pacientes : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Creo la variable root para mandar a traer los elementos de la vista

        val root = inflater.inflate(R.layout.fragment_pacientes, container, false)

        val txtNombrePaciente = root.findViewById<EditText>(R.id.txtNombrePaciente)
        val spDoctores = root.findViewById<Spinner>(R.id.spDoctores)
        val txtFechaNacimientos = root.findViewById<EditText>(R.id.txtFechaNacimiento)
        val txtDireccion = root.findViewById<EditText>(R.id.txtDireccionPaciente)

        //-2 Funcion para obtener los datos
        fun obtnerDoctores(): List<dataClassDoctores>{

            //-1 Crear un obj de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery( "select * from tbDoctores")!!

            //Necesito una lista para guardar todos los datos del select
            //Si mi select trae 15 datos esta lista se llenara con esos 15 datos

            val listaDoctores = mutableListOf<dataClassDoctores>()

            while (resultSet.next()){
                val uuid = resultSet.getString("DoctorUUID")
                val nombre = resultSet.getString("nombreDoctor")
                val especialidad = resultSet.getString("especialidad")
                val telefono = resultSet.getString("telefono")
                val UDC = dataClassDoctores(uuid,nombre,especialidad,telefono)
                listaDoctores.add(UDC)
            }

            return listaDoctores

        }

        //Programemos el spinner(ComboBox)
        CoroutineScope(Dispatchers.IO).launch{
            //1- Obtengo la lista de datos que aparecera en el spinner
            val listaDeDoctores = obtnerDoctores()
            // 2- De la variable que tiene todos los valores saco solo los nombres
            val nombreDoctores = listaDeDoctores.map { it.nombreDoctor }

            withContext(Dispatchers.Main){
                //2- Creo y configuro el adaptador
                //2.1 EL adaptador ocupa: contexto, layout y los datos a mostrar
                val miAdaptador = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,nombreDoctores)
                spDoctores.adapter = miAdaptador
            }
        }

        return root

    }


}