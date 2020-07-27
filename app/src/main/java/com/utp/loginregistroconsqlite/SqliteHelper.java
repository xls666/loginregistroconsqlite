package com.utp.loginregistroconsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class SqliteHelper extends SQLiteOpenHelper {

    //NOMBRE DE BASE DE DATOS
    public static final String DATABASE_NAME = "loopwiki.com";

    //VERSION DE BASE DE DATOS
    public static final int DATABASE_VERSION = 1;

    //NOMBRE DE TABLA
    public static final String TABLE_USERS = "users";

    //TABLA USERS COLUMNS
    //ID COLUMNA @primaryKey
    public static final String KEY_ID = "id";

    //COLUMNA user name
    public static final String KEY_USER_NAME = "username";

    //COLUMNA email
    public static final String KEY_EMAIL = "email";

    //COLUMNA password
    public static final String KEY_PASSWORD = "password";

    //SQL para crear tabla de usuarios
    public static final String SQL_TABLE_USERS = " CREATE TABLE " + TABLE_USERS
            + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_USER_NAME + " TEXT, "
            + KEY_EMAIL + " TEXT, "
            + KEY_PASSWORD + " TEXT"
            + " ) ";


    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Crear tabla cuando se llama a oncreate
        sqLiteDatabase.execSQL(SQL_TABLE_USERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //soltar tabla para crear una nueva si la versión de la base de datos se actualiza
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_USERS);
    }

    //usando este método podemos agregar usuarios a la tabla de usuarios
    public void addUser(User user) {

        //obtener una base de datos grabable
        SQLiteDatabase db = this.getWritableDatabase();

        //crear Content de valores para insertar.
        ContentValues values = new ContentValues();

        //Poner nombre de usuario en  @values
        values.put(KEY_USER_NAME, user.userName);

        //Poner correo electrónico en @values
        values.put(KEY_EMAIL, user.email);

        //Poner contraseña en @values
        values.put(KEY_PASSWORD, user.password);

        // insertar fila
        long todo_id = db.insert(TABLE_USERS, null, values);
    }

    public User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                new String[]{KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD},//Seleccionar columnas quiere consultar
                KEY_EMAIL + "=?",
                new String[]{user.email},//clausula where
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            //si el cursor tiene valor, entonces en la base de datos de usuarios hay un usuario asociado con este correo electrónico dado
            User user1 = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

            //Haga coincidir ambas contraseñas, compruebe que sean iguales o no
            if (user.password.equalsIgnoreCase(user1.password)) {
                return user1;
            }
        }

        //si la contraseña del usuario no coincide o no hay registro con ese correo electrónico, devuelva @false
        return null;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Seleccionando Tabla
                new String[]{KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD},//Seleccionando columnas quiere consultar
                KEY_EMAIL + "=?",
                new String[]{email},//clausula where
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            //si el cursor tiene valor, entonces en la base de datos del usuario hay un
            // usuario asociado con este correo electrónico dado, así que devuelve verdadero
            return true;
        }

        //si el correo electrónico no existe, devuelve falso
        return false;
    }
}
