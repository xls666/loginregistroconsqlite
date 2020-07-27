package com.utp.loginregistroconsqlite;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 */

public class RegisterActivity extends AppCompatActivity {

    //Declaracion EditTexts
    EditText editTextUserName;
    EditText editTextEmail;
    EditText editTextPassword;

    //Declaracion TextInputLayout
    TextInputLayout textInputLayoutUserName;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;

    //Declaracion Button
    Button buttonRegister;

    //Declaracion SqliteHelper
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sqliteHelper = new SqliteHelper(this);
        initTextViewLogin();
        initViews();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String UserName = editTextUserName.getText().toString();
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();

                    //Compruebe en la base de datos si hay algún usuario asociado con este correo electrónico.
                    if (!sqliteHelper.isEmailExists(Email)) {

                        //El correo electrónico no existe ahora agregue un nuevo usuario a la base de datos
                        sqliteHelper.addUser(new User(null, UserName, Email, Password));
                        Snackbar.make(buttonRegister, "Usuario creado con éxito! Por favor Iniciar sesión ", Snackbar.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, Snackbar.LENGTH_LONG);
                    }else {

                        //El correo electrónico existe con la entrada de correo electrónico proporcionada, por lo tanto, mostrar el usuario de error ya existe
                        Snackbar.make(buttonRegister, "El usuario ya existe con el mismo correo electrónico. ", Snackbar.LENGTH_LONG).show();
                    }


                }
            }
        });
    }

    //Este método se utiliza para configurar el evento Click TextView de inicio de sesión
    private void initTextViewLogin() {
        TextView textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //Este método se utiliza para conectar vistas XML a sus objetos
    private void initViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutUserName = (TextInputLayout) findViewById(R.id.textInputLayoutUserName);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

    }

    //Este método se utiliza para validar la entrada dada por el usuario
    public boolean validate() {
        boolean valid = false;


        String UserName = editTextUserName.getText().toString();
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();


        if (UserName.isEmpty()) {
            valid = false;
            textInputLayoutUserName.setError("Porfavor ingrese un usuario valido!");
        } else {
            if (UserName.length() > 5) {
                valid = true;
                textInputLayoutUserName.setError(null);
            } else {
                valid = false;
                textInputLayoutUserName.setError("El nombre de usuario es corto!");
            }
        }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            textInputLayoutEmail.setError("¡Por favor introduzca un correo electrónico válido!");
        } else {
            valid = true;
            textInputLayoutEmail.setError(null);
        }

        //Manejo de validación para el campo Contraseña
        if (Password.isEmpty()) {
            valid = false;
            textInputLayoutPassword.setError("Por favor ingrese una contraseña válida!");
        } else {
            if (Password.length() > 5) {
                valid = true;
                textInputLayoutPassword.setError(null);
            } else {
                valid = false;
                textInputLayoutPassword.setError("¡La contraseña es corta!");
            }
        }


        return valid;
    }
}
