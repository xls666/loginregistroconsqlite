package com.utp.loginregistroconsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    //Declaracion EditTexts
    EditText editTextEmail;
    EditText editTextPassword;

    //Declaracion TextInputLayout
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;

    //Declaracion Button
    Button buttonLogin;

    //Declaracion SqliteHelper
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sqliteHelper = new SqliteHelper(this);
        initCreateAccountTextView();
        initViews();

        //establecer evento de clic del botón de inicio de sesión
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Verifique que la entrada del usuario sea correcta o no
                if (validate()) {

                    //Obtenga valores de los campos EditText
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();

                    //usuario autenticado
                    User currentUser = sqliteHelper.Authenticate(new User(null, null, Email, Password));

                    //Revisar si la autenticación es exitosa o no
                    if (currentUser != null) {
                        Snackbar.make(buttonLogin, "Logueado exitosamente!", Snackbar.LENGTH_LONG).show();


                    } else {

                        //User Logged in Failed
                        Snackbar.make(buttonLogin, " Login Fallado, porfavor intentar denuevo", Snackbar.LENGTH_LONG).show();

                    }
                }
            }
        });


    }

    //este método se usa para configurar Crear cuenta Texto de TextView y hacer clic en evento (varios colores
    // para TextView aún no es compatible con Xml, así que lo he hecho mediante programación)
    private void initCreateAccountTextView() {
        TextView textViewCreateAccount = (TextView) findViewById(R.id.textViewCreateAccount);
        textViewCreateAccount.setText(fromHtml("<font color='#ffffff'>Aún no tengo cuenta</font><font color='#0c0099'>crea uno</font>"));
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //Este método se utiliza para conectar vistas XML a sus objetos
    private void initViews() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

    }

    //Este método es para manejar * fromHtml *
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    //Este método se usa para validar la entrada dada por el usuario
    public boolean validate() {
        boolean valid = false;

        //Obtenga valores de los campos EditText
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        //Manejo de validación para campo de correo electrónico
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
