1. Descargar el Backend, cargarlo en VSCODE.
2. Ejecutar el server.js (En caso de tener otra contraseña del root, cambiarla en password: 'tu_contraseña').
3. Para crear la base de datos, primero, tener instalado MySQL Workbench.
4. Luego de estar dentro de SQL, cargar este codigo:
   
drop table users;

CREATE DATABASE proyecto_semestral_db;
USE proyecto_semestral_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255)
);

5. Luego abrir el backend, y ejecutarlo.

Con eso tendriamos listo, y podemos guardar los usuarios.
